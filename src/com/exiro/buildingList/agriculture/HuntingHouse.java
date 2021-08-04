package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

import java.awt.*;
import java.util.ArrayList;

public class HuntingHouse extends ResourceGenerator {

    public HuntingHouse(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public HuntingHouse(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.HUNTINGHOUSE, BuildingCategory.FOOD, pop, 8, 32, 10, xPos, yPos, 2, 2, cases, built, city, 0, Resource.MEAT);
    }

    public HuntingHouse() {
        super(false, ObjectType.HUNTINGHOUSE, BuildingCategory.FOOD, 0, 8, 32, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.MEAT);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 32, 15, getCity(), this);
            s.setOffsetX(10);
            s.setOffsetY(2);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            s.process(delta);
        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
        for (BuildingSprite s : getBuildingSprites()) {
            s.Render(g, camX, camY);
        }
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
    }

    @Override
    protected void addPopulation() {

    }
}
