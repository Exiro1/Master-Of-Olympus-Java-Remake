package com.exiro.fileManager;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * MP3 decoder backed by JLayer. The Steam release of Zeus ships its music as MP3.
 */
public class Mp3Decoder implements MusicDecoder {

    private final File file;
    private Bitstream bitstream;
    private Decoder decoder;

    private int channels;
    private int sampleRate;
    private boolean swap = false;
    private boolean endOfStream = false;

    // Samples of the last decoded frame that did not fit in the caller's buffer.
    private short[] pending = new short[0];
    private int pendingPos = 0;
    private int pendingLen = 0;

    public Mp3Decoder(File file) {
        this.file = file;
    }

    @Override
    public boolean initialize() {
        try {
            bitstream = new Bitstream(new BufferedInputStream(new FileInputStream(file)));
            decoder = new Decoder();
            Header header = bitstream.readFrame();
            if (header == null) {
                System.err.println("No MP3 frame found in " + file);
                close();
                return false;
            }
            sampleRate = header.frequency();
            channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
            decodeFrame(header);
        } catch (IOException | BitstreamException | DecoderException e) {
            e.printStackTrace();
            close();
            return false;
        }
        return true;
    }

    private void decodeFrame(Header header) throws DecoderException, BitstreamException {
        SampleBuffer out = (SampleBuffer) decoder.decodeFrame(header, bitstream);
        int len = out.getBufferLength();
        if (pending.length < len)
            pending = new short[len];
        System.arraycopy(out.getBuffer(), 0, pending, 0, len);
        pendingPos = 0;
        pendingLen = len;
        bitstream.closeFrame();
    }

    /** Decodes the next frame into {@code pending}; returns false at the end of the stream. */
    private boolean nextFrame() {
        try {
            Header header = bitstream.readFrame();
            if (header == null)
                return false;
            decodeFrame(header);
            return true;
        } catch (BitstreamException | DecoderException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int numChannels() {
        return channels;
    }

    @Override
    public int sampleRate() {
        return sampleRate;
    }

    @Override
    public void setSwap(boolean swap) {
        this.swap = swap;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        if (endOfStream)
            return -1;

        int bytes = 0;
        int max = buffer.length & ~1;
        while (bytes < max) {
            if (pendingPos >= pendingLen && !nextFrame()) {
                endOfStream = true;
                close();
                break;
            }
            while (pendingPos < pendingLen && bytes < max) {
                short s = pending[pendingPos++];
                byte hi = (byte) (s >> 8);
                byte lo = (byte) s;
                buffer[bytes++] = swap ? lo : hi;
                buffer[bytes++] = swap ? hi : lo;
            }
        }
        return bytes > 0 ? bytes : -1;
    }

    private void close() {
        if (bitstream != null) {
            try {
                bitstream.close();
            } catch (BitstreamException ignored) {
            }
            bitstream = null;
        }
    }

    @Override
    public void dump() {
        System.err.println("MP3 " + file.getName() + ": " + channels + " channel(s), " + sampleRate + " Hz");
    }
}
