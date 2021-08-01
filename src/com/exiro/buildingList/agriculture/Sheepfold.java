package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Sheepfold extends ResourceGenerator {


    public Sheepfold(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public Sheepfold(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, pop, 15, 50, 10, xPos, yPos, 2, 2, cases, built, city, 0, Resource.WOOL);
    }

    public Sheepfold() {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, 0, 15, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.WOOL);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 56, 12, getCity(), this);
            s.setOffsetX(24);
            s.setOffsetY(2);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }


    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
    }

    @Override
    protected void addPopulation() {

    }
}
