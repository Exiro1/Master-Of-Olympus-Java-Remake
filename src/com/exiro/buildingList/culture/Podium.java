package com.exiro.buildingList.culture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Podium extends Building {


    public Podium(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public Podium() {
        super(false, ObjectType.PODIUM, BuildingCategory.CULTURE, 0, 4, 30, 2, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            city.addPodium(this);
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 84, 24, getCity(), this);
            s.setOffsetX(47);
            s.setOffsetY(6);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    @Override
    public void delete() {
        super.delete();
        city.removePodium(this);
    }
}
