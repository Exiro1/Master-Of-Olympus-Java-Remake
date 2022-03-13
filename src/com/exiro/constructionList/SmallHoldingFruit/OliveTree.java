package com.exiro.constructionList.SmallHoldingFruit;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class OliveTree extends SmallHoldingTree {

    int growLevel = 0;
    float growth = 0;


    public OliveTree(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, int growLevel) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, 2);
        this.growLevel = growLevel;
    }

    public OliveTree(City city) {
        super(false, ObjectType.OLIVETREE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, 2);
    }

    public OliveTree() {
        super(false, ObjectType.OLIVETREE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, 2);
    }

}
