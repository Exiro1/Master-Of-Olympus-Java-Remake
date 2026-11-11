package com.exiro.fileManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Random;

import com.exiro.soundManager.SoundManager;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALException;

/**
 *
 * Streams one music track through OpenAL. Based on the OggVorbis streamer OpenAL tutorial
 * at http://www.devmaster.net/articles/openal-tutorials/lesson8.php
 *
 * Decodes Ogg Vorbis (j-ogg) and MP3 (JLayer). The playlist in sound_fxU.txt names
 * .ogg files while the Steam release ships .mp3, so a track is looked up under
 * both extensions.
 *
 * @author Krishna K Gadepalli
 */
public class MusicStreamer implements Runnable{

    static AL al = SoundLoader.al;

    private static boolean debug = false;
    private static int totalBytes = 0;

    private static void debugMsg(String str) {
        if (debug) System.err.println(str);
    }

    private MusicDecoder decoder;

    // The size of a chunk from the stream that we want to read for each update.
    private static int BUFFER_SIZE = 4096*16;

    // The number of buffers used in the audio pipeline
    private static int NUM_BUFFERS = 2;

    // Buffers hold sound data. There are two of them by default (front/back)
    private int[] buffers = new int[NUM_BUFFERS];

    // Sources are points emitting sound.
    private int[] source = new int[1];

    private int format;	// OpenAL data format
    private int rate;	// sample rate

    // Position, Velocity, Direction of the source sound.
    private float[] sourcePos = { 0.0f, 0.0f, 0.0f };
    private float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    private float[] sourceDir = { 0.0f, 0.0f, 0.0f };

    private String url;

    private long sleepTime = 0;

    private boolean opened = false;

    public MusicStreamer(String url) {
        this.url = "Assets/Audio/Music/"+url;
    }

    /**
     * Returns the track file, trying the other known extension (.ogg/.mp3)
     * when the listed one is missing.
     */
    static File resolveTrack(String path) {
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

    /**
     * Open the music stream and initialize OpenAL based
     * on the stream properties
     */
    public boolean open() throws MalformedURLException {
        File track = resolveTrack(url);
        if (track == null) {
            System.err.println("Music file not found: " + url);
            return false;
        }
        decoder = createDecoder(track);

        if (!decoder.initialize()) {
            System.err.println("Error initializing music stream: " + track);
            return false;
        }

        int numChannels = decoder.numChannels();
        int numBytesPerSample = 2;

        if (numChannels == 1)
            format = AL.AL_FORMAT_MONO16;
        else
            format = AL.AL_FORMAT_STEREO16;

        rate = decoder.sampleRate();

        // A rough estimation of how much time in milliseconds we can sleep
        // before checking to see if the queued buffers have been played
        // (so that we dont peg the CPU by doing an active wait). We divide
        // by 10 at the end to be safe...
        // round it off to the nearest multiple of 10.
        sleepTime = (long)(1000.0 * BUFFER_SIZE /
                numBytesPerSample / numChannels / rate / 10.0);
        sleepTime = (sleepTime + 10)/10 * 10;



        // TODO: I am not if this is the right way to fix the endian
        // problems I am having... but this seems to fix it on Linux
        decoder.setSwap(true);

        al.alGenBuffers(NUM_BUFFERS, buffers, 0); check();
        al.alGenSources(1, source, 0); check();

        al.alSourcefv(source[0], AL.AL_POSITION , sourcePos, 0);
        al.alSourcefv(source[0], AL.AL_VELOCITY , sourceVel, 0);
        al.alSourcefv(source[0], AL.AL_DIRECTION, sourceDir, 0);

        al.alSourcef(source[0], AL.AL_ROLLOFF_FACTOR,  0.0f    );
        al.alSourcei(source[0], AL.AL_SOURCE_RELATIVE, AL.AL_TRUE);

        opened = true;
        return true;
    }

    /**
     * OpenAL cleanup
     */
    public void release() {
        if (!opened)
            return;
        al.alSourceStop(source[0]);
        empty();

        al.alDeleteSources(1, source, 0); check();
        al.alDeleteBuffers(NUM_BUFFERS, buffers, 0); check();
        opened = false;
    }

    /**
     * Play the music stream
     */
    public boolean playback() {
        if (playing())
            return true;

        debugMsg("playback(): stream all buffers");
        for (int i = 0; i < NUM_BUFFERS; i++) {
            if (!stream(buffers[i]))
                return false;
        }

        debugMsg("playback(): queue all buffers & play source");
        al.alSourceQueueBuffers(source[0], NUM_BUFFERS, buffers, 0);
        al.alSourcePlay(source[0]);

        return true;
    }

    /**
     * Check if the source is playing
     */
    public boolean playing() {
        int[] state = new int[1];

        al.alGetSourcei(source[0], AL.AL_SOURCE_STATE, state, 0);

        return (state[0] == AL.AL_PLAYING);
    }

    /**
     * Update the stream if necessary
     */
    public boolean update() {
        int[] processed = new int[1];
        boolean active = true;

        debugMsg("update()");
        al.alGetSourcei(source[0], AL.AL_BUFFERS_PROCESSED, processed, 0);

        while (processed[0] > 0)
        {
            int[] buffer = new int[1];

            al.alSourceUnqueueBuffers(source[0], 1, buffer, 0); check();
            debugMsg("update(): buffer unqueued => " + buffer[0]);

            active = stream(buffer[0]);

            debugMsg("update(): buffer queued => " + buffer[0]);
            al.alSourceQueueBuffers(source[0], 1, buffer, 0); check();

            processed[0]--;
        }

        return active;
    }

    /**
     * Reloads a buffer (reads in the next chunk)
     */
    public boolean stream(int buffer) {
        byte[] pcm = new byte[BUFFER_SIZE];
        int    size = 0;

        try {
            if ((size = decoder.read(pcm)) <= 0)
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        totalBytes += size;
        debugMsg("stream(): buffer data => " + buffer + " totalBytes:" + totalBytes);

        ByteBuffer data = ByteBuffer.wrap(pcm, 0, size);
        al.alBufferData(buffer, format, data, size, rate);
        check();

        return true;
    }

    /**
     * Empties the queue
     */
    protected void empty() {
        int[] queued = new int[1];

        al.alGetSourcei(source[0], AL.AL_BUFFERS_QUEUED, queued, 0);

        while (queued[0] > 0)
        {
            int[] buffer = new int[1];

            al.alSourceUnqueueBuffers(source[0], 1, buffer, 0);
            check();

            queued[0]--;
        }

        decoder = null;
    }

    /**
     * Check for OpenAL errors...
     */
    protected void check() {
        if (al.alGetError() != AL.AL_NO_ERROR)
            throw new ALException("OpenAL error raised...");
    }

    /**
     * The main loop to initialize and play the entire stream
     */
    public boolean playstream() throws MalformedURLException {
        if (!open())
            return false;

        decoder.dump();

        if (!playback())
            return false;

        while (update()) {
            // We will try sleeping for sometime so that we dont
            // peg the CPU...
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (playing()) continue;

            if (!playback())
                return false;
        }

        return true;
    }

    @Override
    public void run() {
        try {
            playstream();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Always schedule the next track, otherwise music stops for good after one failure.
            SoundManager.secBeforeNextMusic = new Random().nextInt(60);
        }
    }
}
