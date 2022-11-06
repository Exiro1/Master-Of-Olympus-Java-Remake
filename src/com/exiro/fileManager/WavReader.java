package com.exiro.fileManager;

import com.jogamp.common.util.Bitstream;
import com.jogamp.common.util.IOUtil;
import com.jogamp.openal.ALConstants;
import com.jogamp.openal.ALException;
import com.jogamp.openal.util.WAVData;
import com.jogamp.openal.util.WAVLoader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WavReader {

    public static void alutLoadWAVFile(final String fileName,
                                       final int[] format,
                                       final ByteBuffer[] data,
                                       final int[] size,
                                       final int[] freq,
                                       final int[] loop) throws ALException {
        try {
            final WAVData wd = loadFromFile(fileName);
            format[0] = wd.format;
            data[0] = wd.data;
            size[0] = wd.size;
            freq[0] = wd.freq;
            loop[0] = wd.loop ? ALConstants.AL_TRUE : ALConstants.AL_FALSE;
        } catch (final Exception e) {
            throw new ALException(e);
        }
    }

    public static WAVData loadFromFile(final String filename) throws ALException, IOException {
        final File soundFile = new File(filename);
        final InputStream is = new FileInputStream(soundFile);
        return loadFromStreamImpl(is);
    }

    private static final int RIFF = 0x52494646;
    private static final int RIFX = 0x52494658;
    private static final int WAVE = 0x57415645;
    private static final int FACT = 0x66616374;
    private static final int FMT  = 0x666D7420;
    private static final int DATA = 0x64617461;

    private static WAVData loadFromStreamImpl(final InputStream aIn) throws ALException, IOException {
        /**
         * references:
         * http://www.sonicspot.com/guide/wavefiles.html
         * https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
         * http://stackoverflow.com/questions/1111539/is-the-endianness-of-format-params-guaranteed-in-riff-wav-files
         * http://sharkysoft.com/archive/lava/docs/javadocs/lava/riff/wave/doc-files/riffwave-content.htm
         */
        final Bitstream.ByteInputStream bis = new Bitstream.ByteInputStream(aIn);
        final Bitstream<InputStream> bs = new Bitstream<InputStream>(bis, false);
        bs.setThrowIOExceptionOnEOF(true);
        try {
            final boolean bigEndian; // FIXME: for all data incl. signatures ?

            final long riffMarker = bs.readUInt32(true /* bigEndian */);
            if ( RIFF == riffMarker ) {
                bigEndian = false;
            } else if( RIFX == riffMarker ) {
                bigEndian = true;
            } else {
                throw new ALException("Invalid RIF header: 0x"+Integer.toHexString((int)riffMarker)+", "+bs);
            }
            final long riffLenL = bs.readUInt32(bigEndian);
            final int riffLenI = Bitstream.uint32LongToInt(riffLenL);
            final long wavMarker = bs.readUInt32(true /* bigEndian */);
            if ( WAVE != wavMarker ) {
                throw new ALException("Invalid WAV header: 0x"+Integer.toHexString((int)wavMarker)+", "+bs);
            }
            boolean foundFmt = false;
            boolean foundData = false;

            short sChannels = 0, sSampleSizeInBits = 0;
            long sampleRate = 0;
            long chunkLength = 0;

            while (!foundData) {
                final int chunkId = (int)bs.readUInt32(true /* bigEndian */);
                chunkLength = bs.readUInt32(bigEndian);
                switch (chunkId) {
                    case FMT:
                        foundFmt = true;
                        @SuppressWarnings("unused")
                        final int compressionCode = bs.readUInt16(bigEndian);
                        sChannels = (short)bs.readUInt16(bigEndian);
                        sampleRate = bs.readUInt32(bigEndian);
                        @SuppressWarnings("unused")
                        final long bytesPerSeconds = bs.readUInt32(bigEndian);
                        @SuppressWarnings("unused")
                        final short blockAlignment = (short) bs.readUInt16(bigEndian);
                        sSampleSizeInBits = (short) bs.readUInt16(bigEndian);
                        bs.skip( 8 * ( chunkLength - 16 ) );
                        break;
                    case FACT:
                        // FIXME: compression format dependent data?
                        bs.skip( 8 * chunkLength );
                        break;
                    case DATA:
                        if (!foundFmt) {
                            throw new ALException("WAV fmt chunks must be before data chunks: "+bs);
                        }
                        foundData = true;
                        break;
                    default:
                        // unrecognized chunk, skips it
                        bs.skip( 8 * chunkLength );
                }
            }

            final int channels = sChannels;
            final int sampleSizeInBits = sSampleSizeInBits;
            final float fSampleRate = sampleRate;
            return loadFromStream(bs.getSubStream(), Bitstream.uint32LongToInt(chunkLength), channels, sampleSizeInBits,
                    Math.round(fSampleRate), bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN, false);
        } finally {
            bs.close();
        }
    }

    public static WAVData loadFromStream(InputStream aIn, final int initialCapacity, final int numChannels, final int bits, final int sampleRate, final ByteOrder byteOrder, final boolean loop)
            throws IOException {
        if( !(aIn instanceof BufferedInputStream) ) {
            aIn = new BufferedInputStream(aIn);
        }
        // ReadableByteChannel aChannel = Channels.newChannel(aIn);
        int format = ALConstants.AL_FORMAT_MONO8;

        if ((bits == 8) && (numChannels == 1)) {
            format = ALConstants.AL_FORMAT_MONO8;
        } else if ((bits == 16) && (numChannels == 1)) {
            format = ALConstants.AL_FORMAT_MONO16;
        } else if ((bits == 8) && (numChannels == 2)) {
            format = ALConstants.AL_FORMAT_STEREO8;
        } else if ((bits == 16) && (numChannels == 2)) {
            format = ALConstants.AL_FORMAT_STEREO16;
        }
        final ByteBuffer buffer = IOUtil.copyStream2ByteBuffer(aIn, initialCapacity);
        buffer.limit(initialCapacity);
        final int size = buffer.limit();

        // Must byte swap in case endianess mismatch
        if ( bits == 16 && ByteOrder.nativeOrder() != byteOrder ) {
            final int len = buffer.remaining();
            for (int i = 0; i < len; i += 2) {
                final byte a = buffer.get(i);
                final byte b = buffer.get(i+1);
                buffer.put(i, b);
                buffer.put(i+1, a);
            }
        }

        final WAVData result = new WAVData(buffer, format, size, sampleRate, loop);
        aIn.close();

        return result;
    }

}
