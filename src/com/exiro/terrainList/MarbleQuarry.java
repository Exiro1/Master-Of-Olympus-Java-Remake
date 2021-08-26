package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.ArrayList;

public class MarbleQuarry extends Terrain {


    public MarbleQuarry(boolean isActive, ObjectType type, boolean isWalkable, int xpos, int ypos, City c, boolean isFloor, boolean isConstructable, boolean blocking) {
        super(isActive, type, isWalkable, xpos, ypos, c, isFloor, isConstructable, blocking);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        return false;
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
