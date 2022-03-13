package com.exiro.constructionList.SmallHoldingFruit;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Vine extends SmallHoldingTree {

    public Vine(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, int monthStart) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, monthStart);
    }

    public Vine(City city) {
        super(false, ObjectType.VINE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, 11);
    }

    public Vine() {
        super(false, ObjectType.VINE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, 11);
    }
}
