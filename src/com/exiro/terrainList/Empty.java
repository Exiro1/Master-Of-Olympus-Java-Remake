package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Empty extends Terrain {

    int number;

    public Empty(int number, City c) {
        super(false, ObjectType.EMPTY, true, 0, 0, c, true, true, false);
        this.number = number;
        this.setLocalID(number);
        updateImg();
    }

    public Empty() {
        super(false, ObjectType.EMPTY, true, 0, 0, GameManager.currentCity, true, true, false);
        this.number = 105;
        this.setLocalID(number);
        updateImg();
    }


    @Override
    public boolean build(int xPos, int yPos) {
        return true;
    }


    @Override
    public void delete() {
    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public void process(double deltaTime) {

    }
}
