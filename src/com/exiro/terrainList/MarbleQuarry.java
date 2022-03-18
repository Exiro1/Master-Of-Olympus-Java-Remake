package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectType;

public class MarbleQuarry extends Terrain {


    public MarbleQuarry(boolean isActive, ObjectType type, boolean isWalkable, int xpos, int ypos, City c, boolean isFloor, boolean isConstructable, boolean blocking) {
        super(type, isWalkable, xpos, ypos, c, isFloor, isConstructable, blocking, 1);
    }


    @Override
    public void process(double deltaTime, int deltaDays) {

    }
}
