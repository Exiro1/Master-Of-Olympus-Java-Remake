package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class OlivePress extends ResourceGenerator {


    //2436

    public OlivePress(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public OlivePress() {
        super(false, ObjectType.OLIVE_PRESS, BuildingCategory.INDUSTRY, 0, 12, 72, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.OLIVE_OIL);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 60, 12, getCity(), this);
            s.setOffsetX(33);
            s.setOffsetY(-7);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    public void process(double deltatime) {
        super.process(deltatime);


    }


    @Override
    protected void addPopulation() {

    }
}
