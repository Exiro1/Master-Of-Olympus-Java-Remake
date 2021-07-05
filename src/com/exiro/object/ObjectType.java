package com.exiro.object;

import com.exiro.buildingList.Granary;
import com.exiro.buildingList.House;
import com.exiro.buildingList.Stock;
import com.exiro.buildingList.WaterWell;
import com.exiro.buildingList.agriculture.*;
import com.exiro.constructionList.OliveTree;
import com.exiro.constructionList.Road;
import com.exiro.fileManager.ImageLoader;
import com.exiro.terrainList.Empty;
import com.exiro.terrainList.Water;
import com.exiro.terrainList.WaterCoast;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public enum ObjectType {


    EMPTY("Rien", false, "Zeus_Terrain", 1, 1, 1, Empty.class),
    ROAD("Route", false, "Zeus_General", 1, 1, 54, Road.class),
    BLOCKABLE_ROAD("Stop", true, "Zeus_General", 1, 10, 75, null),
    HOUSE("Maison", true, "Zeus_General", 2, 8, 14, House.class),
    WATERWELL("Puit", true, "Zeus_General", 2, 9, 0, WaterWell.class),
    STOCK("Stock", true, "Zeus_General", 3, 3, 22, Stock.class),
    GRANARY("Grenier", true, "Zeus_General", 4, 3, 28, Granary.class),
    FARM("Ferme", true, "Zeus_General", 3, 4, 12, Farm.class),
    SMALLHOLDING("Métairie", true, "Zeus_General", 2, 4, 90, SmallHolding.class),
    SHEEPFOLD("Bergerie", true, "Zeus_General", 2, 4, 55, Sheepfold.class),
    DAIRY("Laiterie", true, "Zeus_General", 2, 4, 68, Dairy.class),
    HUNTINGHOUSE("Maison de chasse", true, "Zeus_General", 2, 4, 31, HuntingHouse.class),
    OLIVETREE("Olivier", true, "Zeus_General", 1, 4, 0, OliveTree.class),
    WATERTERRAIN("Eau", true, "Zeus_Terrain", 1, 1, 163, Water.class),
    WATERTCOAST("Cote maritime", true, "Zeus_Terrain", 1, 1, 171, WaterCoast.class),
    FISHERY("Pecherie", true, "Zeus_General", 2, 4, 47, Fishery.class);

    private final Class c;
    private final String name;
    private final boolean block;
    private final int heigth, width;
    private final int size;
    private final String path;
    private BufferedImage img;
    private int bitmapID, localID;

    ObjectType(String name, boolean block, String path, int size, int bitmapID, int localID, Class c) {
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
