package com.exiro.buildingList.delivery;

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
import com.exiro.sprite.delivery.SafetyGuard;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Safety extends Building {


    SafetyGuard guard;
    float timeBeforeDelivery = 0.0f;

    public Safety(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public Safety() {
        super(false, ObjectType.SAFETY, BuildingCategory.SECURITY, 0, 5, 16, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);

    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite guard = new BuildingSprite("Zeus_General", 10, 1, 44, city, this);
            guard.setOffsetX(2);
            guard.setOffsetY(-64);
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
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive() && getPop() > 0) {
            if (getMovingSprites().size() == 0) {
                guard = new SafetyGuard(city, null, getAccess().get(0));
                addSprite(guard);
            } else if (guard.hasArrived && guard.getDestination() == this) {
                timeBeforeDelivery += deltaTime;
                if (timeBeforeDelivery >= 10) {
                    guard = new SafetyGuard(city, null, getAccess().get(0));
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
                    Path p = city.getPathManager().getPathTo(c.getXB(), c.getYB(), this.getAccess().get(0).getxPos(), this.getAccess().get(0).getyPos(), FreeState.ONLY_ACTIVE_ROAD.getI());
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
