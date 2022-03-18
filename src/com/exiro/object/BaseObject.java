package com.exiro.object;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.render.ButtonType;
import com.exiro.render.interfaceList.Interface;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseObject implements Cloneable {

    protected ObjectType type;
    protected TileImage img;
    protected int height, width, size, offx, offy;
    protected int xB, yB;
    protected int xlen, ylen;
    protected int bitmapID, localID;
    protected String path;
    protected Case mainCase;
    protected int interfaceW, interfaceH;

    public BaseObject(ObjectType type, String filename, int size, int bitmapID, int localID, int xlen, int ylen) {
        this.xlen = xlen;
        this.ylen = ylen;
        this.type = type;
        this.size = size;
        this.bitmapID = bitmapID;
        this.localID = localID;
        this.path = filename;
        TileImage img = ImageLoader.getImage(filename, bitmapID, localID);
        assert img != null;
        setImg(img);
    }

    public abstract Interface getInterface();

    public abstract void buttonClickedEvent(ButtonType type, int ID);

    public abstract void Render(Graphics g, int camX, int camY);

    public abstract void process(double deltatime, int deltaDays);

    public void updateImg() {
        TileImage img = ImageLoader.getImage(getPath(), bitmapID, localID);
        assert img != null;
        setImg(img);
    }

    public double distanceTo(ObjectClass obj) {
        return Math.sqrt(Math.pow(getXB() - obj.getXB(), 2) + Math.pow(getYB() - obj.getYB(), 2));
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

    public Case getMainCase() {
        return mainCase;
    }

    public void setMainCase(Case mainCase) {
        this.mainCase = mainCase;
    }

    public int getBitmapID() {
        return bitmapID;
    }

    public void setBitmapID(int bitmapID) {
        this.bitmapID = bitmapID;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getXB() {
        return xB;
    }

    public void setXB(int x) {
        this.xB = x;
    }

    public int getYB() {
        return yB;
    }

    public void setYB(int y) {
        this.yB = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BufferedImage getImg() {
        return img.getImg();
    }

    public void setImg(TileImage img) {
        this.img = img;
        this.setWidth(img.getW());
        this.setHeight(img.getH());
        this.setOffx(img.getOffx());
        this.setOffy(img.getOffy());
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
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

    public ObjectType getBuildingType() {
        return type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}