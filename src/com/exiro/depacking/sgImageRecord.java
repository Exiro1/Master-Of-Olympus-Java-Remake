package com.exiro.depacking;

public class sgImageRecord {

    long offset;
    long length;
    long uncompressed_length;
    /* 4 zero bytes: */
    long invert_offset;
    int width;
    int height;
    int jcp;
    int offx, offy;
    int spriteSize;

    /* 26 unknown bytes, mostly zero, first four are 2 shorts */
    long type;
    /* 4 flag/option-like bytes: */
    short[] flags;
    long bitmap_id;
    /* 3 bytes + 4 zero bytes */
    /* For D6 and up SG3 versions: alpha masks */
    long alpha_offset;
    long alpha_length;

    public sgImageRecord() {
    }

    public sgImageRecord(long offset, long length, long uncompressed_length, long invert_offset, int width, int height, int type, short[] flags, short bitmap_id, long alpha_offset, long alpha_length) {
        this.offset = offset;
        this.length = length;
        this.uncompressed_length = uncompressed_length;
        this.invert_offset = invert_offset;
        this.width = width;
        this.height = height;
        this.type = type;
        this.flags = flags;
        this.bitmap_id = bitmap_id;
        this.alpha_offset = alpha_offset;
        this.alpha_length = alpha_length;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getUncompressed_length() {
        return uncompressed_length;
    }

    public void setUncompressed_length(long uncompressed_length) {
        this.uncompressed_length = uncompressed_length;
    }

    public long getInvert_offset() {
        return invert_offset;
    }

    public void setInvert_offset(long invert_offset) {
        this.invert_offset = invert_offset;
    }

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

    public long getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public short[] getFlags() {
        return flags;
    }

    public void setFlags(short[] flags) {
        this.flags = flags;
    }

    public long getBitmap_id() {
        return bitmap_id;
    }

    public void setBitmap_id(long bitmap_id) {
        this.bitmap_id = bitmap_id;
    }

    public void setBitmap_id(short bitmap_id) {
        this.bitmap_id = bitmap_id;
    }

    public long getAlpha_offset() {
        return alpha_offset;
    }

    public void setAlpha_offset(long alpha_offset) {
        this.alpha_offset = alpha_offset;
    }

    public long getAlpha_length() {
        return alpha_length;
    }

    public void setAlpha_length(long alpha_length) {
        this.alpha_length = alpha_length;
    }

    public int getJcp() {
        return jcp;
    }

    public void setJcp(int jcp) {
        this.jcp = jcp;
    }

    public int getOffx() {
        return offx;
    }

    public void setOffx(int offx) {
        this.offx = offx;
    }

    public int getOffy() {
        return offy;
    }

    public void setOffy(int offy) {
        this.offy = offy;
    }

    public int getSpriteSize() {
        return spriteSize;
    }

    public void setSpriteSize(int spriteSize) {
        this.spriteSize = spriteSize;
    }
}
