package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

public class SculptureStudio extends IndustryConverter {


    public SculptureStudio() {
        super(false, ObjectType.SCULPTURE_STUDIO, BuildingCategory.INDUSTRY, 0, 18, 160, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.SCULPTURE, 60, 1, 4, Resource.BRONZE, 4);
    }

    @Override
    public void createBuildingSpriteWork() {
        BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 86, 35, getCity(), this);
        s.setOffsetX(36);
        s.setOffsetY(-8);
        s.setTimeBetweenFrame(0.08f);
        addSprite(s);
    }

    @Override
    public BuildingSprite createBuildingSpriteWait() {
        BuildingSprite s = super.createBuildingSpriteWait();
        s.setOffsetX(24);
        s.setOffsetY(10);
        return s;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            setState(ConversionState.WAITING_RESOURCES);
            return true;
        }
        return false;
    }

    @Override
    protected void addPopulation() {

    }
}
