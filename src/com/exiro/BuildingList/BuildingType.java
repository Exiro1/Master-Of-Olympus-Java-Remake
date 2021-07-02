package com.exiro.BuildingList;

import com.exiro.ConstructionList.Empty;
import com.exiro.ConstructionList.Road;
import com.exiro.FileManager.ImageLoader;
import com.exiro.Object.ObjectClass;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public enum BuildingType {


    EMPTY("rien", false, "Zeus_Terrain", 1, 1, 1, Empty.class),
    ROAD("route", false, "Zeus_General", 1, 1, 54, Road.class),
    BLOCKABLE_ROAD("Stop", true, "Zeus_General", 1, 10, 75, null),
    HOUSE("Maison", true, "Zeus_General", 2, 8, 14, House.class),
    WATERWELL("Puit", true, "Zeus_General", 2, 9, 0, WaterWell.class),
    STOCK("Stock", true, "Zeus_General", 3, 3, 22, Stock.class),
    GRANARY("Grenier", true, "Zeus_General", 4, 3, 28, Granary.class),
    FARM("Ferme", true, "Zeus_General", 3, 4, 12, Farm.class);

    private final Class c;
    private final String name;
    private final boolean block;
    private final int heigth, width;
    private final int size;
    private final String path;
    private BufferedImage img;
    private int bitmapID, localID;

    BuildingType(String name, boolean block, String path, int size, int bitmapID, int localID, Class c) {
        this.c = c;
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

    public ObjectClass getDefault() {
        try {
            return (ObjectClass) c.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
