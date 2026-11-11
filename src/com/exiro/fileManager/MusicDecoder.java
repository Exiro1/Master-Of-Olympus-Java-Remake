package com.exiro.fileManager;

import java.io.IOException;

/**
 * Streams a compressed music file as 16-bit signed PCM.
 * Samples are big-endian by default; {@link #setSwap(boolean) setSwap(true)}
 * makes {@link #read(byte[])} return little-endian samples, as OpenAL expects.
 */
public interface MusicDecoder {

    boolean initialize();

    int numChannels();

    int sampleRate();

    void setSwap(boolean swap);

    /**
     * Fills the buffer with interleaved PCM samples.
     * @return the number of bytes written, or -1 at the end of the stream
     */
    int read(byte[] buffer) throws IOException;

    void dump();
}
