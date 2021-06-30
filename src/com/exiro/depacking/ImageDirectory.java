package com.exiro.depacking;

import java.util.ArrayList;

public class ImageDirectory {

    ArrayList<TileImage> images;
    int ID;
    int size;
    String filename;

    public ImageDirectory(int ID, int size, String filename) {
        this.images = new ArrayList<>();
        this.ID = ID;
        this.size = size;
        this.filename = filename;
    }

    public void addImage(TileImage t) {
        images.add(t);
    }

    public ArrayList<TileImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<TileImage> images) {
        this.images = images;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
