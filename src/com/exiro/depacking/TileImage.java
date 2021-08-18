package com.exiro.depacking;

import java.awt.image.BufferedImage;

public class TileImage {


    BufferedImage img;
    int w, h;
    int bitID;
    int localID;
    int offx, offy;

    public TileImage(BufferedImage img, int w, int h, int bitID, int localID, int offx, int offy) {
        this.img = img;
        this.w = w;
        this.h = h;
        this.bitID = bitID;
        this.localID = localID;
        this.offx = offx;
        this.offy = offy;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getBitID() {
        return bitID;
    }

    public void setBitID(int bitID) {
        this.bitID = bitID;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public int getOffx() {
        return offx;
    }

    public int getOffy() {
        return offy;
    }
}
