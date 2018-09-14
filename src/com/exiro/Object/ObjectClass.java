package com.exiro.Object;

import com.exiro.BuildingList.BuildingType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ObjectClass implements Cloneable {

    private boolean Active = false;
    private BuildingType type;

    private BufferedImage img;
    private int height, width, size;
    private int xB, yB;


    public ObjectClass(boolean isActive, BuildingType type, String path, int width, int height, int size) {
        this.Active = isActive;
        this.width = width;
        this.height = height;
        this.type = type;
        this.size = size;
        try {
            File imgFile = new File(path);
            img = ImageIO.read(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public abstract BufferedImage getRender();

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

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
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