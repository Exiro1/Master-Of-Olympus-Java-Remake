package com.exiro.BuildingList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum BuildingType {


    EMPTY("rien", false, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_land1_00106.png", 1),
    ROAD("route", false, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_Beautification2_00056.png", 1),
    BLOCKABLE_ROAD("Stop", true, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_Municipal_00076.png", 1),
    HOUSE("Maison", true, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_Housing_00015.png", 2),
    WATERWELL("Puit", true, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_HealthNSan_00001.png", 2),
    STOCK("Stock", true, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\fullStock.png", 3);

    private final String name;
    private final boolean block;
    private final int heigth, width;
    private final int size;
    private BufferedImage img;

    BuildingType(String name, boolean block, String path, int size) {
        this.size = size;
        this.name = name;
        File f = new File(path);
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        heigth = img.getHeight();
        width = img.getWidth();

        this.block = block;
    }

    public boolean isBlocking() {
        return this.block;
    }

    public BufferedImage getImg() {
        return img;
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
