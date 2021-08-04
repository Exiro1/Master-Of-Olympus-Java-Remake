package com.exiro.sprite;

import com.exiro.object.Case;
import com.exiro.object.City;

import java.util.ArrayList;

public class ComplexSprite extends Sprite {


    public ComplexSprite(String filePath, int bitID, int localId, int frameNumber, City c) {
        super(filePath, bitID, localId, frameNumber, c);
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
