package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.ArrayList;

public class Earthquake extends Terrain {

    int nbr;

    public Earthquake(int xpos, int ypos, City c, int nbr) {
        super(true, ObjectType.EARTHQUAKE, false, xpos, ypos, c, true, false, true);
        this.nbr = nbr;
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
