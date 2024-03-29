package com.exiro.terrainList;

import com.exiro.object.BaseObject;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.ButtonType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.utils.Point;

import java.awt.*;

public abstract class Terrain extends BaseObject {

    boolean isWalkable;
    int xPos;
    int yPos;
    City city;
    boolean isFloor;
    boolean constructible;
    boolean blocking;

    public Terrain(ObjectType type, boolean isWalkable, int xpos, int ypos, City c, boolean isFloor, boolean isConstructable, boolean blocking, int size) {
        super(type, type.getPath(), size, type.getBitmapID(), type.getLocalID(), size, size);
        this.isWalkable = isWalkable;
        this.xPos = xpos;
        this.yPos = ypos;
        this.city = c;
        this.isFloor = isFloor;
        this.constructible = isConstructable;
        this.setXB(xpos);
        this.setYB(ypos);
        this.blocking = blocking;
        if (c.getMap() != null)
            this.setMainCase(c.getMap().getCase(xpos, ypos));
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = new BuildingInterface(300, 300, 500, 400, null, this);
        bi.addText(getType().getName(), "Zeus.ttf", 16f, 100, 50);
        bi.addText(getMainCase().getZlvl() + " x:" + getMainCase().getxPos() + " y:" + getMainCase().getyPos(), "Zeus.ttf", 16f, 100, 100);
        return bi;
    }

    @Override
    public void buttonClickedEvent(ButtonType type, int ID) {

    }

    /**
     * Appelés toute les secondes
     */

    boolean test = true;

    @Override
    public void Render(Graphics g, int camX, int camY) {
        int lvl = getMainCase().getZlvl();
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - lvl, getyPos() - lvl), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
    }

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }

    public boolean isConstructible() {
        return constructible;
    }

    public void setConstructible(boolean constructible) {
        this.constructible = constructible;
    }

    public boolean isFloor() {
        return isFloor;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
        this.setXB(xPos);
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
        this.setYB(yPos);
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
