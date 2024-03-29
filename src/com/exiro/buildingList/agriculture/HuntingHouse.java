package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

import java.awt.*;

public class HuntingHouse extends ResourceGenerator {

    double growth = 0;
    int speedFactor = 1;



    public HuntingHouse() {
        super(false, ObjectType.HUNTINGHOUSE, BuildingCategory.FOOD, 0, 8, 32, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.MEAT,4);
        maxPerCarter = 3;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargements de " + getResource().getName(), 16, 20, 80);

        return bi;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 32, 15, getCity(), this);
            s.setOffsetX(-17);
            s.setOffsetY(-49);
            s.setTimeBetweenFrame(0.1f);
            s.setComplex(true);
            addSprite(s);
            return true;
        }
        return false;
    }


    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            s.process(delta, 0);
        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isWorking()) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            growth += factor * deltaTime * speedFactor;
            if (growth > 60) {
                growth = 0;
                resourceCreated(1);
            }
        }
    }

    @Override
    protected void addPopulation() {

    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.HUNTING;
    }
}
