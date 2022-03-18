package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectType;

public class Earthquake extends Terrain {

    int nbr;

    public Earthquake(int xpos, int ypos, City c, int nbr) {
        super(ObjectType.EARTHQUAKE, false, xpos, ypos, c, true, false, true, 1);
        this.nbr = nbr;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {

    }
}
