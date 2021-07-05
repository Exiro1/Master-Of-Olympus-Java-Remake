package com.exiro.object;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ObjectClass implements Cloneable {

    private boolean Active = false;
    private ObjectType type;

    private TileImage img;
    private int height, width, size;
    private int xB, yB;
    private int bitmapID, localID;
    private String path;

    public ObjectClass(boolean isActive, ObjectType type, String filename, int size, int bitmapID, int localID) {
        this.Active = isActive;
        this.type = type;
        this.size = size;
        this.bitmapID = bitmapID;
        this.localID = localID;
        this.path = filename;
        TileImage img = ImageLoader.getImage(filename, bitmapID, localID);
        assert img != null;
        setImg(img);
    }

    public ObjectClass(boolean isActive, ObjectType type) {
        this.Active = isActive;
        this.type = type;
        this.size = type.getSize();
        this.bitmapID = type.getBitmapID();
        this.localID = type.getLocalID();
        this.path = type.getPath();
        TileImage img = ImageLoader.getImage(getPath(), bitmapID, localID);
        assert img != null;
        setImg(img);
    }

    public void updateImg() {
        TileImage img = ImageLoader.getImage(getPath(), bitmapID, localID);
        assert img != null;
        setImg(img);
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

    public abstract boolean build(int xPos, int yPos);

    public abstract void Render(Graphics g, int camX, int camY);

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

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public void setImg(TileImage img) {
        this.img = img;
        this.setWidth(img.getW());
        this.setHeight(img.getH());
    }

    abstract public void delete();

    public ObjectType getBuildingType() {
        return type;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    abstract public ArrayList<Case> getAccess();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}