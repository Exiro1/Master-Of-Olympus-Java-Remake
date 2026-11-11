package com.exiro.fileManager;

import com.jogamp.openal.AL;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cache of decoded WAV files as OpenAL buffers.
 *
 * Files are loaded on a background thread so the game loop never waits on the disk:
 * {@link #get(String)} returns the buffer if it is ready, and otherwise starts loading it
 * and returns -1 (the caller skips this sound or tries again later). Every buffer is kept
 * until {@link #clear()}: the game's effects are small (about 85 MB of 16-bit PCM in total,
 * most of it loaded only when needed).
 */
public final class SoundBank {

    private static final int LOADING = 0;   // OpenAL never hands out buffer id 0
    private static final int FAILED = -2;

    private static final Map<String, Integer> buffers = new ConcurrentHashMap<>();
    private static final ExecutorService loader = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "SoundBank loader");
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        return t;
    });

    private SoundBank() {
    }

    /**
     * Returns the OpenAL buffer holding {@code path}, or -1 while it is still loading
     * or if the file could not be read.
     */
    public static int get(String path) {
        Integer b = buffers.get(path);
        if (b == null) {
            request(path);
            return -1;
        }
        return b > 0 ? b : -1;
    }

    /** True if the file is decoded and ready to play. */
    public static boolean isReady(String path) {
        Integer b = buffers.get(path);
        return b != null && b > 0;
    }

    /** Starts loading the files in the background, in order. */
    public static void preload(Collection<String> paths) {
        for (String p : paths)
            request(p);
    }

    private static void request(String path) {
        if (buffers.putIfAbsent(path, LOADING) == null)
            loader.execute(() -> buffers.put(path, load(path)));
    }

    private static int load(String path) {
        AL al = SoundLoader.al;
        int[] format = new int[1], size = new int[1], freq = new int[1], loop = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        try {
            WavReader.alutLoadWAVFile(path, format, data, size, freq, loop);
        } catch (Exception e) {
            System.err.println("Cannot load sound " + path + ": " + e.getMessage());
            return FAILED;
        }
        int[] id = new int[1];
        synchronized (SoundBank.class) {
            al.alGetError();
            al.alGenBuffers(1, id, 0);
            al.alBufferData(id[0], format[0], data[0], size[0], freq[0]);
            if (al.alGetError() != AL.AL_NO_ERROR) {
                System.err.println("OpenAL could not take sound " + path);
                al.alDeleteBuffers(1, id, 0);
                return FAILED;
            }
        }
        return id[0];
    }

    /** Frees every buffer. Sources must no longer reference them. */
    public static synchronized void clear() {
        for (int b : buffers.values()) {
            if (b > 0)
                SoundLoader.al.alDeleteBuffers(1, new int[]{b}, 0);
        }
        buffers.clear();
    }
}
