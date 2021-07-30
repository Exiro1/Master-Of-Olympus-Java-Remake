package com.exiro.object;

import com.exiro.terrainList.Terrain;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Case {
    private int xPos, yPos;
    private boolean Occuped = false;
    //private ObjectType objectType;
    private ObjectClass object;
    private boolean isMainCase; //case ou dessiner l objet
    private Image img;
    private int width, height;
    private int size;
    private Terrain terrain;

    public Case(int x, int y, ObjectClass obj, Terrain terrain) {
        this.xPos = x;
        this.yPos = y;
        this.object = obj;
        this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isMainCase() {
        return isMainCase;
    }

    public void setMainCase(boolean mainCase) {
        isMainCase = mainCase;
    }

    @Override
    public String toString() {
        return "x : " + xPos + " , y : " + yPos + " occupé : " + Occuped;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }


    public ObjectClass getObject() {
        return object;
    }

    public void setObject(ObjectClass object) {
        this.object = object;
        //this.setBuildingType(object.getBuildingType());
    }

    public boolean isOccuped() {
        return Occuped;
    }

    public void setOccuped(boolean occuped) {
        Occuped = occuped;
    }

    /*public ObjectType getBuildingType() {
        return objectType;
    }

    public void setBuildingType(ObjectType objectType) {
        this.objectType = objectType;
        this.setImg(objectType.getImg());
        this.setSize(objectType.getSize());
        this.setWidth(objectType.getWidth());
        this.setHeight(objectType.getHeigth());
    }
    */

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }


    //TODO ajouter un parametre qui definiera la texture de fond (le vrai sol)

}
