package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

public class OlivePress extends IndustryConverter {


    //2436


    public OlivePress() {
        super(false, ObjectType.OLIVE_PRESS, BuildingCategory.INDUSTRY, 0, 12, 72, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.OLIVE_OIL, 60, 1, 1, Resource.OLIVE, 2);
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
