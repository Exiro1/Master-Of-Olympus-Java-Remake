package com.exiro.buildingList.security;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.delivery.SecurityGuard;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class SecurityStation extends Building {


    SecurityGuard guard;
    float timeBeforeDelivery = 0.0f;

    public SecurityStation(boolean isActive, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, ObjectType.SECURITY_STATION, BuildingCategory.SECURITY, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public SecurityStation() {
        super(false, ObjectType.SECURITY_STATION, BuildingCategory.SECURITY, 0, 5, 16, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);

    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite guard = new BuildingSprite("SprAmbient", 0, 2957, 53, city, this);
            guard.setComplex(true);
            guard.setOffsetX(20);
            guard.setOffsetY(-20);
            guard.setTimeBetweenFrame(0.1f);
            addSprite(guard);
            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta, 0);
        }
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            if (getMovingSprites().size() == 0) {
                guard = new SecurityGuard(city, null, getAccess().get(0));
                addSprite(guard);
            } else if (guard.hasArrived && guard.getDestination() == this) {
                timeBeforeDelivery += deltaTime;
                if (timeBeforeDelivery >= 10) {
                    guard = new SecurityGuard(city, null, getAccess().get(0));
                    addSprite(guard);
                    timeBeforeDelivery = 0;
                }
            }
        }
    }

    @Override
    public void populate(double deltaTime) {
        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : getMovingSprites()) {
            if (c.hasArrived) {
                if (c.getDestination() == this) {
                    toDestroy.add(c);
                } else {
                    Path p = city.getPathManager().getPathTo(c.getXB(), c.getYB(), this.getAccess().get(0).getxPos(), this.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.getI());
                    if (p != null) {
                        c.setRoutePath(p);
                        c.setDestination(this);
                        c.hasArrived = false;
                    }
                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
    }

    @Override
    protected void addPopulation() {

    }

}
