package com.exiro.depacking;

import java.io.IOException;

public class utils {

    static public long readUInt32le(sgFileReader f) throws IOException {
        short[] content = f.readByte(4);
        return ((long) content[0] | (long) content[1] << 8 | ((long) content[2] << 16) | ((long) content[3] << 24));
    }

    static public long readUInt16le(sgFileReader f) {
        short[] content = f.readByte(2);
        return (content[0] | content[1] << 8);
    }

    static public long readUInt8le(sgFileReader f) {
        short[] content = f.readByte(1);
        return (content[0]);
    }

    static public int readInt32le(sgFileReader f) throws IOException {
        short[] content = f.readByte(4);
        return (content[0] | content[1] << 8 | (content[2] << 16) | (content[3] << 24));

    }

    static public int readInt16le(sgFileReader f) {
        short[] content = f.readByte(2);
        return (content[0] | content[1] << 8);
    }

    static public int readInt8le(sgFileReader f) {
        short[] content = f.readByte(1);
        return (content[0]);
    }


}
