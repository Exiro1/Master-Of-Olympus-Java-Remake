package com.exiro.depacking;

import java.io.IOException;
import java.util.ArrayList;

public class sgBitmap {


    int images_n;
    int images_c;
    sgBitmapRecord record;
    String sgFilename;
    int bitmapId;
    ArrayList<sgImage> images;


    static sgBitmapRecord sg_read_bitmap_record(sgFileReader f) throws IOException {
        sgBitmapRecord rec = new sgBitmapRecord();
        rec.filename = f.readString(65);
        //rec.filename[64] = 0;
        rec.comment = f.readString(51);
        //rec.comment[50] = 0;

        rec.width = utils.readUInt32le(f);
        rec.height = utils.readUInt32le(f);
        rec.num_images = utils.readUInt32le(f);
        rec.start_index = utils.readUInt32le(f);
        rec.end_index = utils.readUInt32le(f);
        f.currPosByte += 64;

        return rec;
    }

    static sgBitmap sg_read_bitmap(int id, String sgFilename, sgFileReader file) throws IOException {
        sgBitmap bmp = new sgBitmap();

        bmp.images = null;
        bmp.images_n = 0;
        bmp.images_c = 0;

        bmp.bitmapId = id;
        bmp.sgFilename = sgFilename;
        bmp.record = sg_read_bitmap_record(file);

        return bmp;
    }

    static int sg_get_bitmap_image_count(sgBitmap bmp) {
        return bmp.images_n;
    }

    static void sg_add_bitmap_image(sgBitmap bmp, sgImage child) {
        if (bmp.images_n >= bmp.images_c) {
            int new_cap = bmp.images_c * 2 | 4;
            ArrayList<sgImage> new_arr = new ArrayList<>();
            int i;
            for (i = 0; i < bmp.images_c; i++) {
                new_arr.add(bmp.images.get(i));
            }
            //free(bmp.images);
            bmp.images = new_arr;
            bmp.images_c = new_cap;
        }
        bmp.images_n++;
        bmp.images.add(child);
    }

    void sg_delete_bitmap(sgBitmap bmp) {
        //free(bmp.sgFilename);
        if (bmp.images != null) {
            //free(bmp.images);
        }
        //free(bmp);
    }

    String sg_get_bitmap_filename(sgBitmap bmp) {
        return bmp.record.filename;
    }

    String sg_get_bitmap_comment(sgBitmap bmp) {
        return bmp.record.comment;
    }

    long sg_get_bitmap_width(sgBitmap bmp) {
        return bmp.record.width;
    }

    long sg_get_bitmap_height(sgBitmap bmp) {
        return bmp.record.height;
    }

    long sg_get_bitmap_num_images(sgBitmap bmp) {
        return bmp.record.num_images;
    }

    long sg_get_bitmap_start_index(sgBitmap bmp) {
        return bmp.record.start_index;
    }

    long sg_get_bitmap_end_index(sgBitmap bmp) {
        return bmp.record.end_index;
    }

    String sg_get_bitmap_sg_filename(sgBitmap bmp) {
        return bmp.sgFilename;
    }

    int sg_get_bitmap_id(sgBitmap bmp) {
        return bmp.bitmapId;
    }

    sgImage sg_get_bitmap_image(sgBitmap bmp, int i) {
        if (i < 0 || i >= bmp.images_n) {
            return null;
        }
        return bmp.images.get(i);
    }

    public int getImages_n() {
        return images_n;
    }

    public void setImages_n(int images_n) {
        this.images_n = images_n;
    }

    public int getImages_c() {
        return images_c;
    }

    public void setImages_c(int images_c) {
        this.images_c = images_c;
    }

    public sgBitmapRecord getRecord() {
        return record;
    }

    public void setRecord(sgBitmapRecord record) {
        this.record = record;
    }

    public String getSgFilename() {
        return sgFilename;
    }

    public void setSgFilename(String sgFilename) {
        this.sgFilename = sgFilename;
    }

    public int getBitmapId() {
        return bitmapId;
    }

    public void setBitmapId(int bitmapId) {
        this.bitmapId = bitmapId;
    }

    public ArrayList<sgImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<sgImage> images) {
        this.images = images;
    }
}
