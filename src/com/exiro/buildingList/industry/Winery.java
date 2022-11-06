package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

public class Winery extends IndustryConverter {


    public Winery() {
        super(false, ObjectType.WINERY, BuildingCategory.INDUSTRY, 0, 12, 72, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.WINE, 60, 1, 1, Resource.GRAPE, 2);
    }


    @Override
    public BuildingSprite createBuildingSpriteWait() {
        BuildingSprite s = super.createBuildingSpriteWait();
        s.setOffsetX(50);
        s.setOffsetY(-20);
        return s;
    }

    @Override
    public void createBuildingSpriteWork() {
        BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 73, 12, getCity(), this);
        s.setOffsetX(28);
        s.setOffsetY(3);
        s.setTimeBetweenFrame(0.1f);
        setSprite(0, s);
    }

    @Override
    public void createBSStock() {
        BuildingSprite s = new BuildingSprite("Zeus_General", 7, 93 + stockIn - 1, 1, getCity(), this);
        s.setOffsetX(25);
        s.setOffsetY(10);
        addSprite(s);
    }

    @Override
    public void updateBSStock() {
        getBuildingSprites().get(2).setLocalID(98 + stockIn - 1);
        getBuildingSprites().get(2).updateImg();
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
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.WORKSHOPS;
    }
}
