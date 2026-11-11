package com.exiro.soundManager;

import com.exiro.fileManager.MusicStreamer;
import com.exiro.fileManager.SoundLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Decides which music plays. Owns the track that is playing (and any track still fading out),
 * so music can be stopped, crossfaded to another mode, or have its volume changed.
 *
 * Call {@link #update()} regularly from the game loop; timing uses wall-clock time, so the gaps
 * between tracks do not depend on game speed or pause.
 */
public class MusicManager {

    /** What the city is doing, which picks the playlist. */
    public enum Mode {
        /** Normal city life. */
        CALM(SoundLoader.SoundCategory.GENERAL_MUSIC),
        /** An invasion or battle in progress. */
        COMBAT(SoundLoader.SoundCategory.COMBAT_MUSIC_LONG, SoundLoader.SoundCategory.COMBAT_MUSIC_SHORT),
        /** No music. */
        SILENT();

        final SoundLoader.SoundCategory[] categories;

        Mode(SoundLoader.SoundCategory... categories) {
            this.categories = categories;
        }
    }

    private static final long CROSSFADE_MS = 2500;
    private static final long FADE_IN_MS = 1500;
    /** Silence between two tracks of the same mode, in seconds. */
    private static final int MIN_GAP_S = 15, MAX_GAP_S = 45;
    /** First track starts shortly after the game opens. */
    private static final int FIRST_DELAY_S = 3;

    private final Random random = new Random();
    private final Map<Mode, List<String>> bags = new EnumMap<>(Mode.class);
    private final List<MusicStreamer> fading = new ArrayList<>();

    private Mode mode = Mode.CALM;
    private MusicStreamer current;
    private String lastTrack;
    private long nextTrackAt = System.currentTimeMillis() + FIRST_DELAY_S * 1000L;
    private float volume = 0.6f;
    private boolean enabled = true;

    /** Starts, ends and replaces tracks as needed. Call it from the game loop. */
    public void update() {
        fading.removeIf(MusicStreamer::isFinished);

        if (current != null && current.isFinished()) {
            current = null;
            nextTrackAt = System.currentTimeMillis() + 1000L * (MIN_GAP_S + random.nextInt(MAX_GAP_S - MIN_GAP_S + 1));
        }
        if (current == null && enabled && mode != Mode.SILENT && System.currentTimeMillis() >= nextTrackAt)
            startTrack(FADE_IN_MS);
    }

    /**
     * Switches the playlist. The current track fades out while a track of the new mode fades in.
     * Does nothing if the mode is already active.
     */
    public void setMode(Mode newMode) {
        if (newMode == mode)
            return;
        mode = newMode;
        fadeOutCurrent(CROSSFADE_MS);
        if (enabled && mode != Mode.SILENT)
            startTrack(CROSSFADE_MS);
    }

    public Mode getMode() {
        return mode;
    }

    /** Skips to another track of the current mode, with a crossfade. */
    public void next() {
        fadeOutCurrent(CROSSFADE_MS);
        if (enabled && mode != Mode.SILENT)
            startTrack(CROSSFADE_MS);
    }

    /** Fades the music out and keeps it off until {@link #setEnabled(boolean) setEnabled(true)}. */
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        this.enabled = enabled;
        if (!enabled)
            fadeOutCurrent(CROSSFADE_MS);
        else
            nextTrackAt = System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return enabled;
    }

    /** Music volume, 0 to 1. Applies immediately to the playing track. */
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
        if (current != null)
            current.setVolume(this.volume);
        for (MusicStreamer s : fading)
            s.setVolume(this.volume);
    }

    public float getVolume() {
        return volume;
    }

    /** Stops all music at once, e.g. when the game exits. */
    public void shutdown() {
        enabled = false;
        fadeOutCurrent(0);
        for (MusicStreamer s : fading)
            s.stop(0);
    }

    public boolean isPlaying() {
        return current != null && !current.isFinished();
    }

    private void fadeOutCurrent(long fadeMs) {
        if (current == null)
            return;
        current.stop(fadeMs);
        fading.add(current);
        current = null;
    }

    private void startTrack(long fadeInMs) {
        String track = nextFromBag(mode);
        if (track == null)
            return;
        current = new MusicStreamer(track, volume, fadeInMs);
        Thread t = new Thread(current, "Music " + track);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Shuffle-bag pick: every track of the mode plays once before any repeats,
     * and a new round never starts with the track that just played.
     */
    private String nextFromBag(Mode m) {
        List<String> bag = bags.computeIfAbsent(m, k -> new ArrayList<>());
        if (bag.isEmpty()) {
            for (SoundLoader.SoundCategory c : m.categories)
                bag.addAll(SoundLoader.getSoundPaths(c));
            if (bag.isEmpty())
                return null;
            Collections.shuffle(bag, random);
            if (bag.size() > 1 && bag.get(bag.size() - 1).equals(lastTrack))
                Collections.swap(bag, 0, bag.size() - 1);
        }
        lastTrack = bag.remove(bag.size() - 1);
        return lastTrack;
    }

    /** Live streamers, for tests and debugging. */
    int liveStreamers() {
        int n = current != null && !current.isFinished() ? 1 : 0;
        for (Iterator<MusicStreamer> it = fading.iterator(); it.hasNext(); )
            if (!it.next().isFinished())
                n++;
        return n;
    }
}
