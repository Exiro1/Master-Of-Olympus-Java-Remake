package com.exiro.depacking;

import java.io.IOException;

public class sgImage {

    static int ISOMETRIC_TILE_WIDTH = 58;
    static int ISOMETRIC_TILE_HEIGHT = 30;
    static int ISOMETRIC_TILE_BYTES = 1800;
    static int ISOMETRIC_LARGE_TILE_WIDTH = 78;
    static int ISOMETRIC_LARGE_TILE_HEIGHT = 40;
    static int ISOMETRIC_LARGE_TILE_BYTES = 3200;
    sgImageRecord record;
    sgImageRecord workRecord;
    sgBitmap parent;
    char error;
    boolean invert;
    long imageId;

    static sgImageRecord sg_read_image_record(sgFileReader f, boolean includeAlpha) throws IOException {
        sgImageRecord sgIr = new sgImageRecord();
        sgIr.offset = utils.readUInt32le(f);
        sgIr.length = utils.readUInt32le(f);
        sgIr.uncompressed_length = utils.readUInt32le(f);
        f.currPosByte += 4;
        sgIr.invert_offset = utils.readInt32le(f);
        sgIr.width = utils.readInt16le(f);
        sgIr.height = utils.readInt16le(f);
        f.currPosByte += 26;
        sgIr.type = utils.readUInt16le(f);
        sgIr.flags = f.readByte(4);
        sgIr.bitmap_id = utils.readUInt8le(f);
        f.currPosByte += 7;

        if (includeAlpha) {
            sgIr.alpha_offset = utils.readUInt32le(f);
            sgIr.alpha_length = utils.readUInt32le(f);
        } else {
            sgIr.alpha_offset = sgIr.alpha_length = 0;
        }
        return sgIr;
    }

    static sgImage sg_read_image(int id, sgFileReader f, boolean includeAlpha) throws IOException {
        sgImage image = new sgImage();
        image.imageId = id;
        //image.record = image.workRecord = new sgImageRecord();
        image.record = sg_read_image_record(f, includeAlpha);
        image.workRecord = image.record;
        if (image.record.invert_offset != 0) {
            image.invert = true;
        } else {
            image.invert = false;
        }
        return image;
    }


    static public sgImageData sg_load_image_data(sgImage img, String filename555) throws IOException {

        //System.out.println("Record " + img.imageId + " \n");
        //System.out.println("offset " + img.record.offset + " len " + img.record.length + " len2 " + img.record.uncompressed_length + " \n");
        //System.out.println("invert " + img.record.invert_offset + " w " + img.record.width + " h " + img.record.height + " \n");
        //System.out.println("type " + img.record.type + " flags " + img.record.flags[0] + "  " + img.record.flags[1] + " " + img.record.flags[2] + " " + img.record.flags[3] + " bitmap " + img.record.bitmap_id + " \n");


        sgFileReader file555 = new sgFileReader(filename555);


        short[] buffer = fillBuffer(img, file555);

        int[] pixels = new int[img.workRecord.width * img.workRecord.height];
        int i;
        for (i = 0; i < img.workRecord.width * img.workRecord.height; i++) {
            pixels[i] = 0;
        }

        switch ((int) img.workRecord.type) {
            case 0:
            case 1:
            case 10:
            case 12:
            case 13:
                loadPlainImage(img, pixels, buffer);
                break;

            case 30:
                loadIsometricImage(img, pixels, buffer);
                break;

            case 256:
            case 257:
            case 276:
                loadSpriteImage(img, pixels, buffer);
                break;

            default: {
                return null;
            }
        }

        if (img.workRecord.alpha_length != 0) {
            loadAlphaMask(img, pixels, buffer, (int) img.workRecord.length);
        }

        if (img.invert) {
            mirrorResult(img, pixels);
        }

        sgImageData result = new sgImageData();
        result.width = img.workRecord.width;
        result.height = img.workRecord.height;
        result.rMask = 0x000000ff;
        result.gMask = 0x0000ff00;
        result.bMask = 0x00ff0000;
        result.aMask = 0xff000000;
        result.data = pixels;
        return result;
    }

    static short[] fillBuffer(sgImage img, sgFileReader file) {
        long data_length = img.workRecord.length + img.workRecord.alpha_length;


        // Somehow externals have 1 byte added to their offset
        file.currPosByte = (int) img.workRecord.offset - img.workRecord.flags[0];

        return file.readByte((int) data_length);
    }

    static void loadPlainImage(sgImage img, int[] pixels, short[] buffer) {
        // Check whether the image data is OK
        if (img.workRecord.height * img.workRecord.width * 2 != (int) img.workRecord.length) {
            System.out.println("error loadPlainImage");
            return;
        }

        //ArrayList<Pixel> pxls = new ArrayList<>();
        int i = 0;
        int x, y;
        //BufferedImage imgg = new BufferedImage(img.workRecord.width,img.workRecord.height, BufferedImage.TYPE_INT_ARGB);
        for (y = 0; y < (int) img.workRecord.height; y++) {
            for (x = 0; x < (int) img.workRecord.width; x++, i += 2) {
                set555Pixel(img, pixels, x, y, buffer[i] | (buffer[i + 1] << 8));
                //int rgb = pxls.get(pxls.size()-1).r<<16 | pxls.get(pxls.size()-1).g<<8 |pxls.get(pxls.size()-1).b;
                //imgg.setRGB(x,y,pxls.get(pxls.size()-1).argb);
            }
        }


    }

    static void loadIsometricImage(sgImage img, int[] pixels, short[] buffer) {

        writeIsometricBase(img, pixels, buffer);
        writeTransparentImage(img, pixels, buffer,
                (int) (img.workRecord.length - img.workRecord.uncompressed_length), (int) img.workRecord.uncompressed_length);
    }

    static void loadSpriteImage(sgImage img, int[] pixels, short[] buffer) {
        writeTransparentImage(img, pixels, buffer, (int) img.workRecord.length, 0);
    }

    static void loadAlphaMask(sgImage img, int[] pixels, short[] buffer, int index) {
        int i = 0;
        int x = 0, y = 0, j;
        int width = img.workRecord.width;
        long length = img.workRecord.alpha_length;

        while (i < length) {
            short c = buffer[index + i++];
            if (c == 255) {
                /* The next byte is the number of pixels to skip */
                x += buffer[index + i++];
                while (x >= width) {
                    y++;
                    x -= width;
                }
            } else {
                /* `c' is the number of image data bytes */
                for (j = 0; j < c; j++, i++) {
                    setAlphaPixel(img, pixels, x, y, buffer[index + i]);
                    x++;
                    if (x >= width) {
                        y++;
                        x = 0;
                    }
                }
            }
        }
    }

    static void writeIsometricBase(sgImage img, int[] pixels, short[] buffer) {
        int i = 0, x, y;
        int width, height, height_offset;
        int size = img.workRecord.flags[3];
        int x_offset, y_offset;
        int tile_bytes, tile_height, tile_width;

        width = img.workRecord.width;
        height = (width + 2) / 2; /* 58 . 30, 118 . 60, etc */
        height_offset = img.workRecord.height - height;
        y_offset = height_offset;

        if (size == 0) {
            /* Derive the tile size from the height (more regular than width) */
            /* Note that this causes a problem with 4x4 regular vs 3x3 large: */
            /* 4 * 30 = 120; 3 * 40 = 120 -- give precedence to regular */
            if (height % ISOMETRIC_TILE_HEIGHT == 0) {
                size = height / ISOMETRIC_TILE_HEIGHT;
            } else if (height % ISOMETRIC_LARGE_TILE_HEIGHT == 0) {
                size = height / ISOMETRIC_LARGE_TILE_HEIGHT;
            }
        }

        /* Determine whether we should use the regular or large (emperor) tiles */
        if (ISOMETRIC_TILE_HEIGHT * size == height) {
            /* Regular tile */
            tile_bytes = ISOMETRIC_TILE_BYTES;
            tile_height = ISOMETRIC_TILE_HEIGHT;
            tile_width = ISOMETRIC_TILE_WIDTH;
        } else if (ISOMETRIC_LARGE_TILE_HEIGHT * size == height) {
            /* Large (emperor) tile */
            tile_bytes = ISOMETRIC_LARGE_TILE_BYTES;
            tile_height = ISOMETRIC_LARGE_TILE_HEIGHT;
            tile_width = ISOMETRIC_LARGE_TILE_WIDTH;
        } else {
            System.out.println("Unknown tile size");
            return;
        }

        /* Check if buffer length is enough: (width + 2) * height / 2 * 2bpp */
        if ((width + 2) * height != (int) img.workRecord.uncompressed_length) {
            System.out.println("Data length doesn't match footprint size");
            return;
        }

        i = 0;
        for (y = 0; y < (size + (size - 1)); y++) {
            x_offset = (y < size ? (size - y - 1) : (y - size + 1)) * tile_height;
            for (x = 0; x < (y < size ? y + 1 : 2 * size - y - 1); x++, i++) {
                writeIsometricTile(img, pixels, buffer,
                        x_offset, y_offset, tile_width, tile_height, i * tile_bytes);
                x_offset += tile_width + 2;
            }
            y_offset += tile_height / 2;
        }

    }

    static void writeIsometricTile(sgImage img, int[] pixels, short[] buffer,
                                   int offset_x, int offset_y, int tile_width, int tile_height, int index) {
        int half_height = tile_height / 2;
        int x, y, i = 0;

        for (y = 0; y < half_height; y++) {
            int start = tile_height - 2 * (y + 1);
            int end = tile_width - start;
            for (x = start; x < end; x++, i += 2) {
                set555Pixel(img, pixels, offset_x + x, offset_y + y,
                        (buffer[index + i + 1] << 8) | buffer[index + i]);
            }
        }
        for (y = half_height; y < tile_height; y++) {
            int start = 2 * y - tile_height;
            int end = tile_width - start;
            for (x = start; x < end; x++, i += 2) {
                set555Pixel(img, pixels, offset_x + x, offset_y + y,
                        (buffer[index + i + 1] << 8) | buffer[index + i]);
            }
        }
    }

    static void writeTransparentImage(sgImage img, int[] pixels, short[] buffer, int length, int index) {
        int i = 0;
        int x = 0, y = 0, j;
        int width = img.workRecord.width;

        while (i < length) {
            short c = buffer[index + (i++)];

            if (c == 255) {
                /* The next byte is the number of pixels to skip */
                x += buffer[index + i++];
                while (x >= width) {
                    y++;
                    x -= width;
                }
            } else {
                /* `c' is the number of image data bytes */
                for (j = 0; j < c; j++, i += 2) {
                    set555Pixel(img, pixels, x, y, buffer[i + index] | (buffer[index + i + 1] << 8));
                    x++;
                    if (x >= width) {
                        y++;
                        x = 0;
                    }
                }
            }
        }
    }

    static Pixel set555Pixel(sgImage img, int[] pixels, int x, int y, int color) {
        if (color == 0xf81f) {
            return null;
        }

        int rgb = 0xff000000;

        // Red: bits 11-15, should go to bits 17-24
        rgb |= ((color & 0x7c00) << 9) | ((color & 0x7000) << 4);

        // Green: bits 6-10, should go to bits 9-16
        rgb |= ((color & 0x3e0) << 6) | ((color & 0x300));

        // Blue: bits 1-5, should go to bits 1-8
        rgb |= ((color & 0x1f) << 3) | ((color & 0x1c) >> 2);

        pixels[y * img.workRecord.width + x] = rgb;
        return new Pixel((color & 0x7c00) >> 10, (color & 0x3e0) >> 5, color & 0x1f, 0xff, (int) rgb);
    }

    static void setAlphaPixel(sgImage img, int[] pixels, int x, int y, int color) {
        /* Only the first five bits of the alpha channel are used */
        int alpha = ((color & 0x1f) << 3) | ((color & 0x1c) >> 2);

        int p = y * img.workRecord.width + x;
        pixels[p] = (pixels[p] & 0x00ffffff) | (alpha << 24);
    }

    static void mirrorResult(sgImage img, int[] pixels) {
        int x, y;
        for (x = 0; x < (img.workRecord.width - 1) / 2; x++) {
            for (y = 0; y < img.workRecord.height; y++) {
                int p1 = y * img.workRecord.width + x;
                int p2 = (y + 1) * img.workRecord.width - x - 1;
                int tmp;
                tmp = pixels[p1];
                pixels[p1] = pixels[p2];
                pixels[p2] = tmp;
            }
        }
    }


}
