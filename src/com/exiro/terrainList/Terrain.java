package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.utils.Point;

import java.awt.*;

public abstract class Terrain extends ObjectClass {

    boolean isWalkable;
    int xPos;
    int yPos;
    City city;
    boolean isFloor;
    boolean constructible;
    boolean blocking;

    public Terrain(boolean isActive, ObjectType type, boolean isWalkable, int xpos, int ypos, City c, boolean isFloor, boolean isConstructable, boolean blocking) {
        super(isActive, type);
        this.isWalkable = isWalkable;
        this.xPos = xpos;
        this.yPos = ypos;
        this.city = c;
        this.isFloor = isFloor;
        this.constructible = isConstructable;
        this.setXB(xpos);
        this.setYB(ypos);
        this.blocking = blocking;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = new BuildingInterface(300, 300, 500, 400, null);
        bi.addText(getType().getName(), "Zeus.ttf", 16f, 100, 50);
        return bi;
    }

    /**
     * Appelés toute les secondes
     */
    abstract public void process(double deltaTime);

    @Override
    public void Render(Graphics g, int camX, int camY) {
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), getSize());
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
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
