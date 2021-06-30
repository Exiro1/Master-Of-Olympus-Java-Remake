package com.exiro.Object;

import com.exiro.BuildingList.BuildingType;
import com.exiro.FileManager.ImageLoader;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class ObjectClass implements Cloneable {

    private boolean Active = false;
    private BuildingType type;

    private TileImage img;
    private int height, width, size;
    private int xB, yB;
    private int bitmapID, localID;


    public ObjectClass(boolean isActive, BuildingType type, String filename, int size, int bitmapID, int localID) {
        this.Active = isActive;
        this.type = type;
        this.size = size;
        this.bitmapID = bitmapID;
        this.localID = localID;
        TileImage img = ImageLoader.getImage(filename, bitmapID, localID);
        assert img != null;
        setImg(img);
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

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public void setImg(TileImage img) {
        this.img = img;
        this.setWidth(img.getW());
        this.setHeight(img.getH());
    }

    abstract public void delete();

    public BuildingType getBuildingType() {
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