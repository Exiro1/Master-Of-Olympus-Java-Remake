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

public class Mint extends ResourceGenerator {


    public Mint(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public Mint() {
        super(false, ObjectType.MINT, BuildingCategory.INDUSTRY, 0, 15, 160, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.NULL);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 21, 10, getCity(), this);
            s.setOffsetX(9);
            s.setOffsetY(16);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    protected void addPopulation() {

    }
}
