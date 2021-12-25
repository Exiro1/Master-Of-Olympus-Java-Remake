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



    public MarbleFactory() {
        super(false, ObjectType.MARBLE_QUARRY, BuildingCategory.INDUSTRY, 0, 15, 120, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.MARBLE,1);
        maxPerCarter = 1;
    }

    @Override
    protected void addPopulation() {

    }
}
