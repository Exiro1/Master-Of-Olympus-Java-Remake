package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.depacking.TileImage;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

import java.awt.*;

public class OlivePress extends IndustryConverter {


    //2436

    int lastStockIn = 0;
    TileImage stockImg;

    public OlivePress() {
        super(false, ObjectType.OLIVE_PRESS, BuildingCategory.INDUSTRY, 0, 12, 72, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.OLIVE_OIL, 60, 1, 1, Resource.OLIVE, 3);
        maxPerCarter = 1;
    }

    @Override
    public void createBuildingSpriteWork() {
        BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 60, 12, getCity(), this);
        s.setOffsetX(33);
        s.setOffsetY(-7);
        s.setTimeBetweenFrame(0.1f);
        setSprite(0,s);
    }

    @Override
    public BuildingSprite createBuildingSpriteWait() {
        BuildingSprite s = super.createBuildingSpriteWait();
        s.setOffsetX(25);
        s.setOffsetY(0);
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
    public void process(double deltatime, int deltaDays) {
        super.process(deltatime, deltaDays);

    }

    public void createBSStock() {
        BuildingSprite s = new BuildingSprite("Zeus_General", 7, 101 + stockIn - 1, 1, getCity(), this);
        s.setOffsetX(32);
        s.setOffsetY(2);
        if(getBuildingSprites().size()==1)
            addSprite(s);
        setSprite(1,s);
    }


    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
    }

    @Override
    protected void addPopulation() {

    }
}
