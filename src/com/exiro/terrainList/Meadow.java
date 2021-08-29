package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.ArrayList;
import java.util.Random;

public class Meadow extends Terrain {


    public Meadow(int xpos, int ypos, City c, int terraintype) {
        super(false, ObjectType.MEADOW, true, xpos, ypos, c, true, true, false);

        Random r = new Random();
        setLocalID(120 + r.nextInt(4) * 12 + terraintype);
        updateImg();
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
