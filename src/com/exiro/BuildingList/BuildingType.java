package com.exiro.BuildingList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum BuildingType {


    EMPTY("rien", false, "Assets/Terrain/DefaultLand.png", 1),
    ROAD("route", false, "Assets/Construction/Roads/Road.png", 1),
    BLOCKABLE_ROAD("Stop", true, "Assets/Construction/Roads/BlockingRoad.png", 1),
    HOUSE("Maison", true, "Assets/Building/House/DefaultHouse.png", 2),
    WATERWELL("Puit", true, "Assets/Building/Waterfall/DefaultWaterfall.png", 2),
    STOCK("Stock", true, "Assets/Building/Stock/fullStock.png", 3);

    private final String name;
    private final boolean block;
    private final int heigth, width;
    private final int size;
    private BufferedImage img;
    private final String path;

    BuildingType(String name, boolean block, String path, int size) {
        this.size = size;
        this.name = name;
        this.path = path;
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

    public void setImg(BufferedImage img) {
        this.img = img;
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
