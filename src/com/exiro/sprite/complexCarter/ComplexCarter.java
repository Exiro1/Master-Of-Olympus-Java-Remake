package com.exiro.sprite.complexCarter;

import com.exiro.ai.AI;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.sprite.MovingSprite;

import java.util.ArrayList;

public class ComplexCarter {


    Trolley trolley;
    TrolleyDriver driver;
    TrolleyPuller puller;

    ObjectClass dest;


    public ComplexCarter(City c, ObjectClass destination, Case start, Resource resource, int amount) {
        trolley = new Trolley(c, destination, start.getxPos(), start.getyPos(), this, resource, amount);
        driver = new TrolleyDriver(c, destination, start.getxPos(), start.getyPos(), this);
        puller = new TrolleyPuller(c, destination, start.getxPos(), start.getyPos(), this);
        this.dest = destination;
    }

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

    public ObjectClass getDest() {
        return dest;
    }

    public void setDest(ObjectClass dest) {
        this.dest = dest;
        trolley.setDestination(dest);
        driver.setDestination(dest);
        puller.setDestination(dest);
    }

    public void setPath(Case start, Case dest) {

        driver.setRoutePath(AI.goTo(driver.getC(), start, dest, FreeState.ALL_ROAD.getI()));
        puller.setRoutePath(AI.goTo(puller.getC(), start, dest, FreeState.ALL_ROAD.getI()));
        trolley.setRoutePath(AI.goTo(trolley.getC(), start, dest, FreeState.ALL_ROAD.getI()));

    }

    public ArrayList<MovingSprite> getAllSprites() {
        return null;
    }

    public void delete() {
        trolley.delete();
        driver.delete();
        puller.delete();
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
