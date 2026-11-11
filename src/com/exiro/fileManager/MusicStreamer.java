package com.exiro.fileManager;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALException;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;

/**
 * Streams one music track through OpenAL on its own thread.
 * Based on the OggVorbis streamer OpenAL tutorial
 * at http://www.devmaster.net/articles/openal-tutorials/lesson8.php (Krishna K Gadepalli).
 *
 * Decodes Ogg Vorbis (j-ogg) and MP3 (JLayer). The playlist in sound_fxU.txt names
 * .ogg files while the Steam release ships .mp3, so a track is looked up under
 * both extensions.
 *
 * The track can be faded in, faded out and stopped from any thread; {@link com.exiro.soundManager.MusicManager}
 * owns the streamers and decides what plays.
 */
public class MusicStreamer implements Runnable {

    static AL al = SoundLoader.al;

    // Size of the chunk decoded for each buffer: 64 KB is about 0.37 s of 44.1 kHz stereo.
    private static final int BUFFER_SIZE = 4096 * 16;
    // Four buffers queued, so about 1.5 s of audio is ready ahead of the playhead.
    private static final int NUM_BUFFERS = 4;
    // How often the streaming thread refills buffers and updates the fade.
    private static final long POLL_MS = 20;

    private final int[] buffers = new int[NUM_BUFFERS];
    private final int[] source = new int[1];

    private final String path;
    private MusicDecoder decoder;
    private int format;
    private int rate;
    private boolean opened = false;

    private volatile float volume;
    private final long fadeInMs;
    private long startedAt;

    private volatile long fadeOutMs = -1;   // >= 0 once stop was requested
    private volatile long fadeOutStart;
    private volatile boolean finished = false;

    /**
     * @param track    file name relative to Assets/Audio/Music
     * @param volume   gain between 0 and 1
     * @param fadeInMs fade-in duration, 0 to start at full volume
     */
    public MusicStreamer(String track, float volume, long fadeInMs) {
        this.path = "Assets/Audio/Music/" + track;
        this.volume = volume;
        this.fadeInMs = fadeInMs;
    }

    /** Fades the track out over {@code fadeMs} milliseconds, then stops it. Safe to call from any thread. */
    public void stop(long fadeMs) {
        if (fadeOutMs >= 0 && fadeMs >= fadeOutMs)
            return; // already fading out at least as fast
        fadeOutStart = System.currentTimeMillis();
        fadeOutMs = Math.max(0, fadeMs);
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    /** True once the track ended or was stopped and its OpenAL resources are freed. */
    public boolean isFinished() {
        return finished;
    }

    /** True once a stop was requested (the track may still be fading out). */
    public boolean isStopping() {
        return fadeOutMs >= 0;
    }

    public String getPath() {
        return path;
    }

    /**
     * Returns the track file, trying the other known extension (.ogg/.mp3)
     * when the listed one is missing.
     */
    public static File resolveTrack(String path) {
        File f = new File(path);
        if (f.exists())
            return f;
        int dot = path.lastIndexOf('.');
        String base = dot >= 0 ? path.substring(0, dot) : path;
        for (String ext : new String[]{".mp3", ".ogg"}) {
            File alt = new File(base + ext);
            if (alt.exists())
                return alt;
        }
        return null;
    }

    private static MusicDecoder createDecoder(File file) throws MalformedURLException {
        if (file.getName().toLowerCase().endsWith(".mp3"))
            return new Mp3Decoder(file);
        return new OggDecoder(file.toURI().toURL());
    }

    private boolean open() throws MalformedURLException {
        File track = resolveTrack(path);
        if (track == null) {
            System.err.println("Music file not found: " + path);
            return false;
        }
        decoder = createDecoder(track);
        if (!decoder.initialize()) {
            System.err.println("Error initializing music stream: " + track);
            return false;
        }
        format = decoder.numChannels() == 1 ? AL.AL_FORMAT_MONO16 : AL.AL_FORMAT_STEREO16;
        rate = decoder.sampleRate();
        // Decoders produce big-endian samples; OpenAL wants little-endian.
        decoder.setSwap(true);

        al.alGenBuffers(NUM_BUFFERS, buffers, 0); check();
        al.alGenSources(1, source, 0); check();
        opened = true;

        float[] zero = {0f, 0f, 0f};
        al.alSourcefv(source[0], AL.AL_POSITION, zero, 0);
        al.alSourcefv(source[0], AL.AL_VELOCITY, zero, 0);
        al.alSourcefv(source[0], AL.AL_DIRECTION, zero, 0);
        al.alSourcef(source[0], AL.AL_ROLLOFF_FACTOR, 0.0f);
        al.alSourcei(source[0], AL.AL_SOURCE_RELATIVE, AL.AL_TRUE);
        startedAt = System.currentTimeMillis();
        applyGain();
        return true;
    }

    /** Current gain: the volume scaled by the fade-in and fade-out ramps. */
    private float currentGain() {
        long now = System.currentTimeMillis();
        float f = 1f;
        if (fadeInMs > 0)
            f = Math.min(f, (now - startedAt) / (float) fadeInMs);
        long out = fadeOutMs;
        if (out == 0)
            f = 0f;
        else if (out > 0)
            f = Math.min(f, 1f - (now - fadeOutStart) / (float) out);
        return volume * Math.max(0f, Math.min(1f, f));
    }

    private void applyGain() {
        al.alSourcef(source[0], AL.AL_GAIN, currentGain());
    }

    private boolean fadedOut() {
        long out = fadeOutMs;
        return out >= 0 && System.currentTimeMillis() - fadeOutStart >= out;
    }

    private boolean playing() {
        int[] state = new int[1];
        al.alGetSourcei(source[0], AL.AL_SOURCE_STATE, state, 0);
        return state[0] == AL.AL_PLAYING;
    }

    /** Decodes the next chunk into {@code buffer}; returns false at the end of the track. */
    private boolean stream(int buffer) {
        byte[] pcm = new byte[BUFFER_SIZE];
        int size;
        try {
            if ((size = decoder.read(pcm)) <= 0)
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        al.alBufferData(buffer, format, ByteBuffer.wrap(pcm, 0, size), size, rate);
        check();
        return true;
    }

    /** Fills and queues the initial buffers; returns false if the track is empty. */
    private boolean prime() {
        int queued = 0;
        for (int i = 0; i < NUM_BUFFERS; i++) {
            if (!stream(buffers[i]))
                break;
            queued++;
        }
        if (queued == 0)
            return false;
        al.alSourceQueueBuffers(source[0], queued, buffers, 0); check();
        al.alSourcePlay(source[0]);
        return true;
    }

    /** Refills the buffers OpenAL has finished playing; returns false once the decoder is exhausted. */
    private boolean refill() {
        int[] processed = new int[1];
        al.alGetSourcei(source[0], AL.AL_BUFFERS_PROCESSED, processed, 0);
        boolean active = true;
        while (processed[0] > 0) {
            int[] buffer = new int[1];
            al.alSourceUnqueueBuffers(source[0], 1, buffer, 0); check();
            if (active && stream(buffer[0])) {
                al.alSourceQueueBuffers(source[0], 1, buffer, 0); check();
            } else {
                active = false;
            }
            processed[0]--;
        }
        return active;
    }

    private void playStream() throws MalformedURLException {
        if (!open())
            return;
        decoder.dump();
        if (!prime())
            return;

        boolean decoding = true;
        while (!fadedOut()) {
            if (decoding)
                decoding = refill();
            applyGain();
            if (!playing()) {
                if (!decoding)
                    break;              // last buffer played: the track is over
                al.alSourcePlay(source[0]); // buffer underrun after a hitch: resume
            }
            try {
                Thread.sleep(POLL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void release() {
        if (!opened)
            return;
        al.alSourceStop(source[0]);
        // Stopping marks every queued buffer as processed, so they can all be unqueued.
        int[] queued = new int[1];
        al.alGetSourcei(source[0], AL.AL_BUFFERS_QUEUED, queued, 0);
        while (queued[0]-- > 0) {
            int[] buffer = new int[1];
            al.alSourceUnqueueBuffers(source[0], 1, buffer, 0);
        }
        al.alDeleteSources(1, source, 0);
        al.alDeleteBuffers(NUM_BUFFERS, buffers, 0);
        al.alGetError();
        opened = false;
        decoder = null;
    }

    private void check() {
        if (al.alGetError() != AL.AL_NO_ERROR)
            throw new ALException("OpenAL error raised...");
    }

    @Override
    public void run() {
        try {
            playStream();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finished = true;
        }
    }
}
