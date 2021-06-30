package com.exiro.depacking;

import java.io.IOException;

public class sgFile {

    int SG_HEADER_SIZE = 680;
    int SG_BITMAP_RECORD_SIZE = 200;

    sgBitmap[] bitmaps;
    int bitmaps_n;
    sgImage[] images;
    int images_n;
    String filename;
    sgHeader header;


    public sgFile(String filename) throws IOException {
        //sgFile sgf = new sgFile();
        this.filename = filename;
        this.bitmaps = null;
        this.bitmaps_n = 0;
        this.images = null;
        this.images_n = 0;

        sgFileReader file = new sgFileReader(filename);

        if (file == null) {
            System.out.println("Unable to open file\n");
            //return null;
        }

        this.header = sg_read_header(file);


        //System.out.println("Read header, num bitmaps = " + this.header.num_bitmap_records + ", num images = " + this.header.num_image_records);

        loadBitmaps(this, file);

        int pos = SG_HEADER_SIZE + maxBitmapRecords(this) * SG_BITMAP_RECORD_SIZE;
        file.currPosByte = pos;

        loadImages(this, file, this.header.version >= 0xd6);

        if (this.bitmaps_n > 1 && this.images_n == sgBitmap.sg_get_bitmap_image_count(this.bitmaps[0])) {
            //System.out.println("SG file has " + this.bitmaps_n + " bitmaps but only the first is in use\n");
            // Remove the bitmaps other than the first
            int i;

            sgBitmap temp = this.bitmaps[0];
            this.bitmaps = new sgBitmap[1];
            this.bitmaps[0] = temp;
            this.bitmaps_n = 1;
        }
        file.delete();

    }

    public sgHeader sg_read_header(sgFileReader f) throws IOException {
        sgHeader h = new sgHeader();
        h.sg_filesize = utils.readUInt32le(f);
        h.version = utils.readUInt32le(f);
        h.unknown1 = utils.readUInt32le(f);
        h.max_image_records = utils.readInt32le(f);
        h.num_image_records = utils.readInt32le(f);
        h.num_bitmap_records = utils.readInt32le(f);
        h.num_bitmap_records_without_system = utils.readInt32le(f);
        h.total_filesize = utils.readUInt32le(f);
        h.filesize_555 = utils.readUInt32le(f);
        h.filesize_external = utils.readUInt32le(f);
        f.currPosByte = SG_HEADER_SIZE;
        return h;
    }

    void sg_delete_file(sgFile file) {
        int i;
        for (i = 0; i < file.bitmaps_n; i++) {
            //sg_delete_bitmap(file.bitmaps[i]);
        }
        //free(file.bitmaps);
        for (i = 0; i < file.images_n; i++) {
            //sg_delete_image(file.images[i]);
        }
        //free(file.images);
        //free(file.filename);
        //free(file.header);
    }

    void loadBitmaps(sgFile sgf, sgFileReader file) throws IOException {
        if (sgf.bitmaps != null) {
            //free(sgf.bitmaps);
            sgf.bitmaps_n = 0;
        }
        sgf.bitmaps_n = sgf.header.num_bitmap_records;
        sgf.bitmaps = new sgBitmap[sgf.bitmaps_n];

        int i;
        for (i = 0; i < sgf.header.num_bitmap_records; i++) {
            sgBitmap bitmap = sgBitmap.sg_read_bitmap(i, sgf.filename, file);
            sgf.bitmaps[i] = bitmap;
        }
    }

    void loadImages(sgFile sgf, sgFileReader file, boolean includeAlpha) throws IOException {
        if (sgf.images != null) {
            //free(sgf.images);
            sgf.images_n = 0;
        }
        sgf.images_n = sgf.header.num_image_records;
        sgf.images = new sgImage[sgf.images_n];

        // The first one is a dummy/null record
        sgImage.sg_read_image(0, file, includeAlpha);

        int i;
        for (i = 0; i < sgf.header.num_image_records; i++) {
            sgImage image = sgImage.sg_read_image(i + 1, file, includeAlpha);
            long invertOffset = image.workRecord.invert_offset;
            if (invertOffset < 0 && (i + invertOffset) >= 0) {
                image.workRecord = sgf.images[(int) (i + invertOffset)].record;
            }
            int bitmapId = (int) image.workRecord.bitmap_id;
            if (bitmapId >= 0 && bitmapId < sgf.bitmaps_n) {
                sgBitmap.sg_add_bitmap_image(sgf.bitmaps[bitmapId], image);
                image.parent = sgf.bitmaps[bitmapId];
            } else {
                //System.out.println("Image " + i + " has no parent: " + bitmapId + "\n");
            }

            sgf.images[i] = image;
        }
    }

    boolean checkVersion(sgFile file) throws IOException {
        if (file.header.version == 0xd3) {
            // SG2 file: filesize = 74480 or 522680 (depending on whether it's
            // a "normal" sg2 or an enemy sg2
            if (file.header.sg_filesize == 74480 || file.header.sg_filesize == 522680) {
                return true;
            }
        } else if (file.header.version == 0xd5 || file.header.version == 0xd6) {
            // SG3 file: filesize = the actual size of the sg3 file
            sgFileReader fp = new sgFileReader(file.filename);
            long filesize = fp.f.length();
            if (file.header.sg_filesize == 74480 || filesize == file.header.sg_filesize) {
                return true;
            }
        }

        // All other cases:
        return false;
    }

    int maxBitmapRecords(sgFile file) {
        if (file.header.version == 0xd3) {
            return 100; // SG2
        } else {
            return 200; // SG3
        }
    }

    long sg_get_file_version(sgFile file) {
        return file.header.version;
    }

    long sg_get_file_total_filesize(sgFile file) {
        return file.header.total_filesize;
    }

    long sg_get_file_555_filesize(sgFile file) {
        return file.header.filesize_555;
    }

    long sg_get_file_external_filesize(sgFile file) {
        return file.header.filesize_external;
    }

    int sg_get_file_bitmap_count(sgFile file) {
        return file.bitmaps_n;
    }

    sgBitmap sg_get_file_bitmap(sgFile file, int i) {
        if (i < 0 || i >= file.bitmaps_n) {
            return null;
        }

        return file.bitmaps[i];
    }

    int sg_get_file_image_count(sgFile file) {
        return file.images_n;
    }

    sgImage sg_get_file_image(sgFile file, int i) {
        if (i < 0 || i >= file.images_n) {
            return null;
        }

        return file.images[i];
    }

    public sgBitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(sgBitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getBitmaps_n() {
        return bitmaps_n;
    }

    public void setBitmaps_n(int bitmaps_n) {
        this.bitmaps_n = bitmaps_n;
    }

    public sgImage[] getImages() {
        return images;
    }

    public void setImages(sgImage[] images) {
        this.images = images;
    }

    public int getImages_n() {
        return images_n;
    }

    public void setImages_n(int images_n) {
        this.images_n = images_n;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public sgHeader getHeader() {
        return header;
    }

    public void setHeader(sgHeader header) {
        this.header = header;
    }
}
