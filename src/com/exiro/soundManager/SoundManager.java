package com.exiro.soundManager;

import com.exiro.fileManager.SoundBank;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.City;
import com.jogamp.openal.AL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Plays sound effects and owns the {@link MusicManager}.
 *
 * Positions are in isometric world pixels (see {@link #tileToWorld}), the same space the
 * renderer draws in, and the listener sits above the centre of the screen. A sound therefore
 * gets quieter as it leaves the view and pans left or right with its place on screen.
 *
 * Three kinds of effects:
 * - ambient: one sound of a building or terrain near the screen centre, chosen by an
 *   {@link AmbientPicker}, with up to {@link #MAX_AMBIENT} playing at once;
 * - background (Layer1): a quiet, non-positional nature loop picked at random;
 * - events: one-shot sounds such as a click or a building being placed ({@link SoundEvent}).
 *
 * All methods must be called from the game loop thread.
 */
public class SoundManager {

    /** One-shot effects, with the file each one plays. */
    public enum SoundEvent {
        BUTTON("button.wav", false),
        PLACE_BUILDING("place_building.wav", true),
        COLLAPSE("collapse.wav", true),
        FIRE("Fire.wav", true);

        final String path;
        final boolean positional;

        SoundEvent(String file, boolean positional) {
            this.path = "Assets/Audio/Wavs/" + file;
            this.positional = positional;
        }
    }

    /** What an {@link AmbientPicker} chose: a sound category and the tile it comes from. */
    public record AmbientPick(SoundLoader.SoundCategory category, float tileX, float tileY) {
    }

    /** Chooses the next ambient sound, normally a building or terrain tile near the screen centre. */
    public interface AmbientPicker {
        /** @return the pick, or null for no ambient sound this time */
        AmbientPick pick(Random random);
    }

    // Isometric half-tile size in pixels, as in IsometricRender.
    private static final float TILE_W_HALF = 29f, TILE_H_HALF = 15f;

    /** Listener distance from the map plane: softens panning for sounds close to the centre. */
    private static final float LISTENER_HEIGHT = 250f;
    /** Full volume within this distance of the listener, silent beyond MAX_DISTANCE (world pixels). */
    private static final float REFERENCE_DISTANCE = 350f, MAX_DISTANCE = 1400f;

    private static final int MAX_SOURCES = 24;
    private static final int MAX_AMBIENT = 3;
    private static final float AMBIENT_GAIN = 0.7f, LAYER1_GAIN = 0.35f, EVENT_GAIN = 1f;
    private static final long AMBIENT_GAP_MIN_MS = 400, AMBIENT_GAP_MAX_MS = 2500;
    private static final long LAYER1_GAP_MIN_MS = 2000, LAYER1_GAP_MAX_MS = 9000;
    private static final long EVENT_REPEAT_MS = 80;

    private enum Kind {AMBIENT, LAYER1, EVENT}

    private static final class Voice {
        final int source;
        Kind kind;
        float baseGain;

        Voice(int source) {
            this.source = source;
        }
    }

    private final AL al = SoundLoader.al;
    private final Random random = new Random();
    private final List<Voice> voices = new ArrayList<>();
    private final MusicManager music = new MusicManager();

    private static SoundManager instance;

    private AmbientPicker ambientPicker;
    private City listenedCity;
    private float effectsVolume = 0.8f;
    private boolean effectsEnabled = true;
    private long nextAmbientAt, nextLayer1At;
    private final long[] lastEventAt = new long[SoundEvent.values().length];

    public SoundManager() {
        instance = this;
        al.alDistanceModel(AL.AL_LINEAR_DISTANCE_CLAMPED);
        al.alListenerfv(AL.AL_VELOCITY, new float[]{0f, 0f, 0f}, 0);
        // Facing the map plane (+z) from below it, with the top of the screen as "up" (-y in world
        // pixels), so the listener's right is +x, the right of the screen.
        al.alListenerfv(AL.AL_ORIENTATION, new float[]{0f, 0f, 1f, 0f, -1f, 0f}, 0);
        updateListener(0f, 0f);

        List<String> preload = new ArrayList<>();
        for (SoundEvent e : SoundEvent.values())
            preload.add(e.path);
        // Ambient sounds are what plays most; load them all up front, in the background.
        for (SoundLoader.SoundCategory c : SoundLoader.SoundCategory.values()) {
            if (c == SoundLoader.SoundCategory.NULL || isMusic(c))
                continue;
            for (String f : SoundLoader.getSoundPaths(c))
                preload.add(pathOf(c, f));
        }
        SoundBank.preload(preload);

        long now = System.currentTimeMillis();
        nextAmbientAt = now + 1000;
        nextLayer1At = now + 500;
    }

    /** Converts a tile position to the isometric world pixels used for sound positions. */
    public static float[] tileToWorld(float tileX, float tileY) {
        return new float[]{(tileX - tileY) * TILE_W_HALF, (tileX + tileY) * TILE_H_HALF};
    }

    /**
     * Puts the listener above the given point, in world pixels.
     * For the screen centre: {@code (viewWidth / 2 - cameraX, viewHeight / 2 - cameraY)}.
     */
    public void updateListener(float worldX, float worldY) {
        al.alListenerfv(AL.AL_POSITION, new float[]{worldX, worldY, -LISTENER_HEIGHT}, 0);
    }

    /** The sound manager of the running game, or null before it is created (e.g. in the map editor). */
    public static SoundManager get() {
        return instance;
    }

    /**
     * Sets the city on screen. Events from other cities are not heard, and switching
     * cities cuts the old city's sounds.
     */
    public void setListenedCity(City city) {
        if (city != listenedCity && listenedCity != null)
            stopEffects();
        listenedCity = city;
    }

    /** Plays an event that happens in {@code city}, if that city is on screen. */
    public static void playIn(City city, SoundEvent event, float tileX, float tileY) {
        SoundManager sm = instance;
        if (sm != null && city != null && city == sm.listenedCity)
            sm.play(event, tileX, tileY);
    }

    /** Plays a UI sound such as a button click. */
    public static void playUi(SoundEvent event) {
        SoundManager sm = instance;
        if (sm != null)
            sm.play(event);
    }

    /** Sets where ambient sounds come from; null stops them. */
    public void setAmbientPicker(AmbientPicker picker) {
        this.ambientPicker = picker;
    }

    // ---------------------------------------------------------------- playback

    /** Plays a one-shot event at a tile; non-positional events ignore the tile. */
    public void play(SoundEvent event, float tileX, float tileY) {
        if (!effectsEnabled)
            return;
        long now = System.currentTimeMillis();
        if (now - lastEventAt[event.ordinal()] < EVENT_REPEAT_MS)
            return; // the same event fired twice in a row (e.g. a drag): play it once
        lastEventAt[event.ordinal()] = now;
        int buffer = SoundBank.get(event.path);
        if (buffer < 0)
            return;
        float[] w = tileToWorld(tileX, tileY);
        start(buffer, Kind.EVENT, EVENT_GAIN, event.positional, w[0], w[1]);
    }

    /** Plays a non-positional event, e.g. a UI click. */
    public void play(SoundEvent event) {
        play(event, 0f, 0f);
    }

    /**
     * Legacy entry point: music categories switch the music mode, any other category plays
     * one of its sounds at the given tile.
     */
    public void playSound(SoundLoader.SoundCategory category, float tileX, float tileY) {
        if (isMusic(category)) {
            music.setMode(category == SoundLoader.SoundCategory.GENERAL_MUSIC ? MusicManager.Mode.CALM : MusicManager.Mode.COMBAT);
            return;
        }
        playCategory(category, Kind.AMBIENT, AMBIENT_GAIN, true, tileX, tileY);
    }

    public void playSound(SoundLoader.SoundCategory category) {
        playSound(category, 0f, 0f);
    }

    private boolean playCategory(SoundLoader.SoundCategory c, Kind kind, float gain, boolean positional, float tileX, float tileY) {
        if (c == null || c == SoundLoader.SoundCategory.NULL || !effectsEnabled)
            return false;
        List<String> files = SoundLoader.getSoundPaths(c);
        if (files.isEmpty())
            return false;
        int buffer = SoundBank.get(pathOf(c, files.get(random.nextInt(files.size()))));
        if (buffer < 0)
            return false;
        float[] w = tileToWorld(tileX, tileY);
        return start(buffer, kind, gain, positional, w[0], w[1]);
    }

    private boolean start(int buffer, Kind kind, float gain, boolean positional, float worldX, float worldY) {
        Voice v = freeVoice(kind);
        if (v == null)
            return false;
        v.kind = kind;
        v.baseGain = gain;
        int s = v.source;
        al.alSourceStop(s);
        al.alSourcei(s, AL.AL_BUFFER, buffer);
        if (positional) {
            al.alSourcei(s, AL.AL_SOURCE_RELATIVE, AL.AL_FALSE);
            al.alSourcefv(s, AL.AL_POSITION, new float[]{worldX, worldY, 0f}, 0);
            al.alSourcef(s, AL.AL_REFERENCE_DISTANCE, REFERENCE_DISTANCE);
            al.alSourcef(s, AL.AL_MAX_DISTANCE, MAX_DISTANCE);
            al.alSourcef(s, AL.AL_ROLLOFF_FACTOR, 1f);
        } else {
            // Glued to the listener: same volume and centred wherever the camera is.
            al.alSourcei(s, AL.AL_SOURCE_RELATIVE, AL.AL_TRUE);
            al.alSourcefv(s, AL.AL_POSITION, new float[]{0f, 0f, 0f}, 0);
            al.alSourcef(s, AL.AL_ROLLOFF_FACTOR, 0f);
        }
        al.alSourcef(s, AL.AL_GAIN, gain * effectsVolume);
        al.alSourcePlay(s);
        return true;
    }

    /** A stopped source to reuse, a new one, or (for events only) the oldest ambient sound. */
    private Voice freeVoice(Kind kind) {
        for (Voice v : voices)
            if (!isPlaying(v.source))
                return v;
        if (voices.size() < MAX_SOURCES) {
            int[] id = new int[1];
            al.alGetError();
            al.alGenSources(1, id, 0);
            if (al.alGetError() == AL.AL_NO_ERROR) {
                Voice v = new Voice(id[0]);
                voices.add(v);
                return v;
            }
        }
        if (kind == Kind.EVENT) {
            for (Voice v : voices)
                if (v.kind != Kind.EVENT)
                    return v; // what you just did matters more than background sound
        }
        return null;
    }

    private boolean isPlaying(int source) {
        int[] state = new int[1];
        al.alGetSourcei(source, AL.AL_SOURCE_STATE, state, 0);
        return state[0] == AL.AL_PLAYING || state[0] == AL.AL_PAUSED;
    }

    private int playing(Kind kind) {
        int n = 0;
        for (Voice v : voices)
            if (v.kind == kind && isPlaying(v.source))
                n++;
        return n;
    }

    // ---------------------------------------------------------------- scheduling

    /** Starts ambient and background sounds when their time comes, and drives the music. Call it every tick. */
    public void update() {
        music.update();
        if (!effectsEnabled)
            return;
        long now = System.currentTimeMillis();

        if (now >= nextAmbientAt && ambientPicker != null && playing(Kind.AMBIENT) < MAX_AMBIENT) {
            AmbientPick p = ambientPicker.pick(random);
            if (p != null)
                playCategory(p.category(), Kind.AMBIENT, AMBIENT_GAIN, true, p.tileX(), p.tileY());
            nextAmbientAt = now + between(AMBIENT_GAP_MIN_MS, AMBIENT_GAP_MAX_MS);
        }

        if (now >= nextLayer1At && playing(Kind.LAYER1) == 0) {
            playCategory(SoundLoader.SoundCategory.LAYER1, Kind.LAYER1, LAYER1_GAIN, false, 0f, 0f);
            nextLayer1At = now + between(LAYER1_GAP_MIN_MS, LAYER1_GAP_MAX_MS);
        }
    }

    private long between(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min));
    }

    // ---------------------------------------------------------------- settings

    /** Volume of every sound effect, 0 to 1. */
    public void setEffectsVolume(float volume) {
        effectsVolume = Math.max(0f, Math.min(1f, volume));
        for (Voice v : voices)
            al.alSourcef(v.source, AL.AL_GAIN, v.baseGain * effectsVolume);
    }

    public float getEffectsVolume() {
        return effectsVolume;
    }

    public void setEffectsEnabled(boolean enabled) {
        effectsEnabled = enabled;
        if (!enabled)
            stopEffects();
    }

    public boolean isEffectsEnabled() {
        return effectsEnabled;
    }

    public void stopEffects() {
        for (Voice v : voices)
            al.alSourceStop(v.source);
    }

    public MusicManager getMusic() {
        return music;
    }

    public void stopMusic() {
        music.setEnabled(false);
    }

    public void playNewMusic() {
        music.next();
    }

    /** Stops everything and frees the OpenAL sources and buffers. */
    public void exit() {
        music.shutdown();
        stopEffects();
        for (Voice v : voices) {
            al.alSourcei(v.source, AL.AL_BUFFER, AL.AL_NONE);
            al.alDeleteSources(1, new int[]{v.source}, 0);
        }
        voices.clear();
        SoundBank.clear();
    }

    // ---------------------------------------------------------------- helpers

    private static boolean isMusic(SoundLoader.SoundCategory c) {
        return c == SoundLoader.SoundCategory.GENERAL_MUSIC
                || c == SoundLoader.SoundCategory.COMBAT_MUSIC_SHORT
                || c == SoundLoader.SoundCategory.COMBAT_MUSIC_LONG;
    }

    /** File path of one sound of a category, as listed in sound_fxU.txt. */
    private static String pathOf(SoundLoader.SoundCategory c, String file) {
        return (c == SoundLoader.SoundCategory.LAYER1 ? "Assets/Audio/Ambient/Layer1/" : "Assets/Audio/Ambient/Layer2/") + file;
    }

    /** Effect voices currently playing, for tests and debugging. */
    int playingEffects() {
        int n = 0;
        for (Voice v : voices)
            if (isPlaying(v.source))
                n++;
        return n;
    }
}
