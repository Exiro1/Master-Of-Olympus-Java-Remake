package com.exiro.object;

public abstract class MapObject extends ObjectClass {


    int xlen, ylen;

    public MapObject(boolean isActive, ObjectType type, String filename, int size, int bitmapID, int localID, int xlen, int ylen) {
        super(isActive, type, filename, size, bitmapID, localID, xlen, ylen);
        this.xlen = xlen;
        this.ylen = ylen;
    }

    public MapObject(boolean isActive, ObjectType type, int xlen, int ylen) {
        super(isActive, type, xlen, ylen);
        this.xlen = xlen;
        this.ylen = ylen;
    }

    public int getXlen() {
        return xlen;
    }

    public void setXlen(int xlen) {
        this.xlen = xlen;
    }

    public int getYlen() {
        return ylen;
    }

    public void setYlen(int ylen) {
        this.ylen = ylen;
    }
}
