package com.exiro.environment;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.Interface;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class Environment extends ObjectClass {

    int xPos;
    int yPos;
    boolean isWalkable;
    City city;
    boolean isFloor;

    public Environment(ObjectType type, boolean isWalkable, int xpos, int ypos, City c, boolean isFloor) {
        super(true, type);
        this.isWalkable = isWalkable;
        this.xPos = xpos;
        this.yPos = ypos;
        setXB(xpos);
        setYB(ypos);
        this.setMainCase(c.getMap().getCase(xPos, yPos));
        c.getMap().getCase(xPos, yPos).setObject(this);
        c.getMap().getCase(xPos, yPos).setOccuped(true);
        this.city = c;
        this.isFloor = isFloor;
    }

    public boolean isFloor() {
        return isFloor;
    }

    @Override
    public Interface getInterface() {
        return null;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        this.setMainCase(city.getMap().getCase(xPos, yPos));
        return false;
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
    }

    @Override
    public void delete() {

    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
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
