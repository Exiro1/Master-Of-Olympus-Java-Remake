package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;
import com.exiro.Sprite.BuildingSprite;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;

import java.util.ArrayList;

public class Granary extends Building {


    public Granary(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, pop, 15, 50, 10, xPos, yPos, 4, 4, cases, built, city, 0);
    }

    public Granary() {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, 0, 15, 50, 10, 0, 0, 4, 4, null, false, GameManager.currentCity, 0);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite("Zeus_General", 3, 29, 16, city, this);
            s.setTimeBetweenFrame(0.1f);
            s.setOffsetX(111);
            s.setOffsetY(31);
            addSprite(s);
        }
        return succ;
    }


    public boolean canStock(Ressource r, int ammount) {
        return true;
    }

    public boolean stock(Ressource r, int ammount) {
        return true;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {
        if (isActive() && getPop() > 0) {

        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    void addPopulation() {

    }

}
