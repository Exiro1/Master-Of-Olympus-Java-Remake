package com.exiro.object;

import com.exiro.moveRelated.RoadMap;
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
    private int zlvl;

    //NORD EST SUD OUEST
    Case[] neighbour;

    public Case(int x, int y, ObjectClass obj, Terrain terrain) {
        this.xPos = x;
        this.yPos = y;
        this.object = obj;
        this.terrain = terrain;
        zlvl = 0;
    }

    public Case(int x, int y, int zlvl, ObjectClass obj, Terrain terrain) {
        this.xPos = x;
        this.yPos = y;
        this.object = obj;
        this.terrain = terrain;
        this.zlvl = zlvl;
    }

    public void initNeighbour(CityMap map) {
        this.neighbour = new Case[4];
        this.neighbour[0] = map.getCase(getxPos(), getyPos() - 1);
        this.neighbour[1] = map.getCase(getxPos() + 1, getyPos());
        this.neighbour[2] = map.getCase(getxPos(), getyPos() + 1);
        this.neighbour[3] = map.getCase(getxPos() - 1, getyPos());
    }

    public Case getNeighbourIndex(int freeState, int index, CityMap map) {
        int i = 0;
        for (Case c : neighbour) {
            if (c != null && (RoadMap.getFreeState(c.xPos, c.yPos, map).getI() & freeState) != 0) {
                if (i == index)
                    return c;
                i++;
            }
        }
        return null;
    }

    public int getNeighbourCount(int freeState, CityMap map) {
        int count = 0;
        for (Case c : neighbour) {
            if (c != null && (RoadMap.getFreeState(c.xPos, c.yPos, map).getI() & freeState) != 0) {
                count++;
            }
        }
        return count;
    }

    public Case[] getNeighbour() {
        return neighbour;
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
        return "x : " + xPos + " , y : " + yPos + " objet : " + (object == null ? "vide" : object.getBuildingType()) + " terrain : " + terrain.getBuildingType();
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

    public int getZlvl() {
        return zlvl;
    }

    public void setZlvl(int zlvl) {
        this.zlvl = zlvl;
    }

    //TODO ajouter un parametre qui definiera la texture de fond (le vrai sol)

}
