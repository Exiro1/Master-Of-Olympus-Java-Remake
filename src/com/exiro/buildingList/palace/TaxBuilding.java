package com.exiro.buildingList.palace;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

public class TaxBuilding extends Building {


    public TaxBuilding() {
        super(false, ObjectType.TAX, BuildingCategory.ARMY, 0, 8, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite("Zeus_General", 10, 46, 18, city, this);
            s.setTimeBetweenFrame(0.1f);
            s.setOffsetX(111);
            s.setOffsetY(31);
            addSprite(s);
        }
        return succ;
    }


    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta, 0);
        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }
}
