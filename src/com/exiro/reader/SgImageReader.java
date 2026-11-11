package com.exiro.reader;

import java.io.IOException;
import java.util.HashMap;

/**
 * Same result as {@link sgImage#sg_load_image_data}, but the .555 file is read once and kept.
 *
 * sg_load_image_data builds a new sgFileReader for every image, and that constructor reads the
 * whole file (SprMain.555 is 15 MB, held as a 30 MB short[]). Every walker frame seen for the
 * first time cost a full file read. fillBuffer moves the reader's position itself, so one reader
 * per file can serve every image.
 *
 * This class lives in the SGReaderJava package to reach its package-private loaders; the real fix
 * belongs in SGReaderJava, after which this class can go.
 */
public final class SgImageReader {

    private static final HashMap<String, sgFileReader> readers = new HashMap<>();

    private SgImageReader() {
    }

    public static sgImageData loadImageData(sgImage img, String filename555) throws IOException {
        sgFileReader reader = readers.get(filename555);
        if (reader == null) {
            reader = new sgFileReader(filename555);
            readers.put(filename555, reader);
        }
        short[] buffer = sgImage.fillBuffer(img, reader);

        sgImageRecord rec = img.workRecord;
        int[] pixels = new int[rec.width * rec.height];
        switch ((int) rec.type) {
            case 0, 1, 10, 12, 13 -> sgImage.loadPlainImage(img, pixels, buffer);
            case 30 -> sgImage.loadIsometricImage(img, pixels, buffer);
            case 256, 257, 276 -> sgImage.loadSpriteImage(img, pixels, buffer);
            default -> {
                return null;
            }
        }
        if (rec.alpha_length != 0)
            sgImage.loadAlphaMask(img, pixels, buffer, (int) rec.length);
        if (img.invert)
            sgImage.mirrorResult(img, pixels);

        sgImageData data = new sgImageData();
        data.width = rec.width;
        data.height = rec.height;
        data.rMask = 0xFF;
        data.gMask = 0xFF00;
        data.bMask = 0xFF0000;
        data.aMask = 0xFF000000;
        data.data = pixels;
        return data;
    }
}
