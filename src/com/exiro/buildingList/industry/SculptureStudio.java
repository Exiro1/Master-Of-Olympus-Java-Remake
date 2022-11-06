package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.fileManager.SoundLoader;
import com.exiro.reader.TileImage;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

import java.awt.*;

public class SculptureStudio extends IndustryConverter {

    TileImage stockImg;

    public SculptureStudio() {
        super(false, ObjectType.SCULPTURE_STUDIO, BuildingCategory.INDUSTRY, 0, 18, 160, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.SCULPTURE, 60, 1, 4, Resource.BRONZE, 4);
        maxPerCarter = 1;
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
        s.setOffsetX(15);
        s.setOffsetY(5);
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

    public void createBSStock() {
        BuildingSprite s = new BuildingSprite("Zeus_General", 7, 93 + stockIn - 1, 1, getCity(), this);
        s.setOffsetX(25);
        s.setOffsetY(10);
        addSprite(s);
    }

    @Override
    public void updateBSStock() {
        getBuildingSprites().get(2).setLocalID(93 + stockIn - 1);
        getBuildingSprites().get(2).updateImg();
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
    }

    @Override
    protected void addPopulation() {

    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.WORKSHOPS;
    }
}
