package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;

public class SmallHolding extends ResourceGenerator {


    private double growth;
    private float speedFactor = 0.8f;

    public SmallHolding() {
        super(false, ObjectType.SMALLHOLDING, BuildingCategory.FOOD, 0, 12, 40, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.OLIVE,3);
        maxPerCarter = 1;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargement d'" + getResource().getName(), 16, 20, 80);

        return bi;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 91, 10, getCity(), this);
            s.setOffsetX(38);
            s.setOffsetY(1);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }


    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            factor = 1;
            growth += factor * deltaTime * speedFactor;

            if (growth > 20) {
                growth = 0;
                resourceCreated(1);
            }

        }
    }

    @Override
    protected void addPopulation() {

    }
}
