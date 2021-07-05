package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.ArrayList;

public class Water extends Terrain {

    int frameNumber = 8;
    int index = 0;
    double timeSinceLastFrame = 0;
    double timeBetweenFrame = 0.1f;

    public Water(int xpos, int ypos, City city) {
        super(true, ObjectType.WATERTERRAIN, false, xpos, ypos, city);
        city.addTerrain(this);
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
        timeSinceLastFrame += deltaTime;
        if (timeSinceLastFrame > timeBetweenFrame) {
            timeSinceLastFrame = 0;
            index++;
            if (index >= frameNumber) {
                index = 0;
            }
            this.setLocalID(getType().getLocalID() + index);
            updateImg();
        }

    }
}
