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
import com.exiro.sprite.delivery.WaterDelivery;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class WaterWell extends Building {

    WaterDelivery waterDelivery;
    float timeBeforeDelivery = 0.0f;

    public WaterWell(boolean isActive, int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(isActive, ObjectType.WATERWELL, BuildingCategory.FOOD, pop, 4, 30, 10, xPos, yPos, 2, 2, cases, built, city, 0);
        waterDelivery = null;
    }

    public WaterWell() {
        super(false, ObjectType.WATERWELL, BuildingCategory.FOOD, 0, 4, 30, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);

    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite water = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 1, 6, city, this);
            water.setOffsetX(30);
            water.setOffsetY(-10);
            water.setTimeBetweenFrame(0.1f);
            addSprite(water);
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
                waterDelivery = new WaterDelivery(city, null, getAccess().get(0));
                addSprite(waterDelivery);
            } else if (waterDelivery.hasArrived && waterDelivery.getDestination() == this) {
                timeBeforeDelivery += deltaTime;
                if (timeBeforeDelivery >= 10) {
                    waterDelivery = new WaterDelivery(city, null, getAccess().get(0));
                    addSprite(waterDelivery);
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
