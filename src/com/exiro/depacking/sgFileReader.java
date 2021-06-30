package com.exiro.depacking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class sgFileReader {

    String path;
    int currPosByte;
    short[] data;
    File f;

    public sgFileReader(String path) throws IOException {
        this.path = path;
        currPosByte = 0;
        f = new File(path);
        byte[] blist = Files.readAllBytes(f.toPath());
        data = new short[blist.length];
        int i = 0;
        for (byte b : blist) {
            data[i] = (short) Byte.toUnsignedInt(b);
            i++;
        }

    }

    void delete() {
        data = null;
    }

    long read(int nbr) {
        long r = 0;
        for (int i = 0; i < nbr; i++) {
            r = r | (long) data[currPosByte + i] << 8 * i;
        }
        currPosByte += nbr;
        return r;
    }

    String readString(int size) {
        char[] slice = new char[size];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = (char) data[currPosByte + i];
        }
        currPosByte += size;
        return String.valueOf(slice);
    }

    short[] readByte(int nbr) {
        short[] slice = new short[nbr];
        for (int i = 0; i < slice.length; i++) {
            slice[i] = ((short) ((0x000000FF) & data[currPosByte + i]));
        }
        currPosByte += nbr;
        return slice;
    }


}
