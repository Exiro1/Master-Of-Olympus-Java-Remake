package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.Random;

public class Meadow extends Terrain {


    public Meadow(int xpos, int ypos, City c, int terraintype) {
        super(ObjectType.MEADOW, true, xpos, ypos, c, true, true, false, 1);

        Random r = new Random();
        setLocalID(120 + r.nextInt(4) * 12 + terraintype);
        updateImg();
    }


    @Override
    public void process(double deltaTime, int deltaDays) {

    }
}
