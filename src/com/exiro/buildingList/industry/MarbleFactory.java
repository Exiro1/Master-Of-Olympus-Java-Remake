package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class MarbleFactory extends ResourceGenerator {


    public MarbleFactory(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public MarbleFactory() {
        super(false, ObjectType.MARBLE_QUARRY, BuildingCategory.INDUSTRY, 0, 15, 120, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.MARBLE);
    }

    @Override
    protected void addPopulation() {

    }
}
