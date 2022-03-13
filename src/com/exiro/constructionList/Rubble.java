package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Rubble extends Construction {


    public Rubble(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, boolean isFloor) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, isFloor);
    }

    public Rubble() {
        super(false, ObjectType.RUBBLE, new ArrayList<>(), 0, 0, 0, 0, 1, 1, 0f, GameManager.currentCity, true, true);
    }

    @Override
    public void process(double deltatime, int deltaDays) {

    }
}
