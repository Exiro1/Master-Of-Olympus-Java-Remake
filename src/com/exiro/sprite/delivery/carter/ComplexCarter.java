package com.exiro.sprite.delivery.carter;

import com.exiro.buildingList.Building;
import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ComplexCarter extends Carter{



    Trolley trolley;
    TrolleyDriver driver;
    TrolleyPuller puller;


    public ComplexCarter(City c, ObjectClass destination, Case start, Resource resource, int amount,int command,int currentDelivery,Building origin) {
        super("SprMain", 0, 1336,0,c,destination,resource,amount,command,currentDelivery,origin);
        trolley = new Trolley(c, destination, start.getxPos(), start.getyPos(), this);
        driver = new TrolleyDriver(c, destination, start.getxPos(), start.getyPos(), this);
        puller = new TrolleyPuller(c, destination, start.getxPos(), start.getyPos(), this);
        Case ca = origin.getAccess().get(0);
        x = ca.getxPos();
        y = ca.getyPos();
        setXB(Math.round(x));
        setYB(Math.round(y));
    }

    @Override
    public void setImage(Direction direction, int frame) {
        if(trolley != null)
            trolley.setImage(direction, 0);
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
        driver.Render(g,camX,camY);
        puller.Render(g,camX,camY);
        trolley.Render(g,camX,camY);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        driver.process(deltaTime);
        puller.process(deltaTime);
        trolley.process(deltaTime);
        hasArrived = trolley.hasArrived;
    }

    @Override
    public void setRoutePath(Path p) {
        trolley.setRoutePath(p);
        driver.setRoutePath(p);
        puller.setRoutePath(p);
    }

    public void setArrived(boolean arrived) {
        trolley.hasArrived = arrived;
        driver.hasArrived = arrived;
        puller.hasArrived = arrived;
    }

    public boolean hasArrived(){
        return trolley.hasArrived;
    }

    public void setDest(ObjectClass dest) {
        trolley.setDestination(dest);
        driver.setDestination(dest);
        puller.setDestination(dest);
    }


    public ArrayList<MovingSprite> getAllSprites() {
        return null;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        return false;
    }


    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    public void delete() {
        trolley.delete();
        driver.delete();
        puller.delete();
    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }


    public Trolley getTrolley() {
        return trolley;
    }

    public TrolleyDriver getDriver() {
        return driver;
    }

    public TrolleyPuller getPuller() {
        return puller;
    }
}
