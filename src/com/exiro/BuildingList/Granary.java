package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.SystemCore.GameManager;

import java.util.ArrayList;

public class Granary extends Building {


    public Granary(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, BuildingType.GRANARY, "Zeus_General", 4, 3, 28, BuildingCategory.MARKET, pop, 15, 50, 10, xPos, yPos, 4, 4, cases, built, city, 0);
    }

    static public Granary DEFAULT() {
        Granary g = new Granary(0, 0, 0, null, false, GameManager.currentCity);
        return g;
    }

    @Override
    public void process(double deltaTime) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    void addPopulation() {

    }

}
