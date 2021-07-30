package com.exiro.buildingList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class WaterWell extends Building {


    public WaterWell(boolean isActive, int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(isActive, ObjectType.WATERWELL, BuildingCategory.FOOD, pop, 2, 50, 10, xPos, yPos, 2, 2, cases, built, city, 0);

    }

    public WaterWell() {
        super(false, ObjectType.WATERWELL, BuildingCategory.FOOD, 0, 2, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);

    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite water = new BuildingSprite(type.getPath(), type.getBitmapID(), 1, 6, city, this);
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
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

}