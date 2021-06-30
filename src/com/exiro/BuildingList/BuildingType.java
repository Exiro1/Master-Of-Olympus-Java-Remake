package com.exiro.BuildingList;

import com.exiro.FileManager.ImageLoader;

import java.awt.image.BufferedImage;

public enum BuildingType {


    EMPTY("rien", false, "Zeus_Terrain", 1, 1, 1),
    ROAD("route", false, "Zeus_General", 1, 1, 54),
    BLOCKABLE_ROAD("Stop", true, "Zeus_General", 1, 10, 75),
    HOUSE("Maison", true, "Zeus_General", 2, 8, 14),
    WATERWELL("Puit", true, "Zeus_General", 2, 9, 0),
    STOCK("Stock", true, "Zeus_General", 3, 3, 22),
    GRANARY("Grenier", true, "Zeus_General", 4, 3, 28);

    private final String name;
    private final boolean block;
    private final int heigth, width;
    private final int size;
    private final String path;
    private BufferedImage img;
    private int bitmapID, localID;

    BuildingType(String name, boolean block, String path, int size, int bitmapID, int localID) {
        this.size = size;
        this.name = name;
        this.path = path;
        img = ImageLoader.getImage(path, bitmapID, localID).getImg();
        this.bitmapID = bitmapID;
        this.localID = localID;
        heigth = img.getHeight();
        width = img.getWidth();

        this.block = block;
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

    public boolean isBlocking() {
        return this.block;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getHeigth() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public boolean isBlock() {
        return block;
    }

    public int getSize() {
        return size;
    }
}
