package com.exiro.Object;

import com.exiro.BuildingList.BuildingType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Case {
    private int xPos, yPos;
    private boolean Occuped = false;
    private BuildingType buildingType;
    private ObjectClass object;
    private boolean isMainCase; //case ou dessiner l objet
    private Image img;
    private int width, height;
    private int size;

    public Case(int x, int y, BuildingType buildingType, ObjectClass obj) {
        this.xPos = x;
        this.yPos = y;
        this.buildingType = buildingType;
        this.object = obj;
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
    }

    public boolean isOccuped() {
        return Occuped;
    }

    public void setOccuped(boolean occuped) {
        Occuped = occuped;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
        this.setImg(buildingType.getImg());
        this.setSize(buildingType.getSize());
        this.setWidth(buildingType.getWidth());
        this.setHeight(buildingType.getHeigth());
    }

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
