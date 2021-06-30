package com.exiro.depacking;

public class sgImageData {

    int width, height;
    int rMask, gMask, bMask, aMask;
    int[] data;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getrMask() {
        return rMask;
    }

    public void setrMask(int rMask) {
        this.rMask = rMask;
    }

    public int getgMask() {
        return gMask;
    }

    public void setgMask(int gMask) {
        this.gMask = gMask;
    }

    public int getbMask() {
        return bMask;
    }

    public void setbMask(int bMask) {
        this.bMask = bMask;
    }

    public int getaMask() {
        return aMask;
    }

    public void setaMask(int aMask) {
        this.aMask = aMask;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }
}
