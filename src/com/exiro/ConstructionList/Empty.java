package com.exiro.ConstructionList;

import com.exiro.BuildingList.BuildingType;
import com.exiro.Object.Case;
import com.exiro.Object.City;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Empty extends Construction {


    public Empty(boolean isActive, BuildingType type, String path, int width, int height, int size, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built) {
        super(isActive, type, path, width, height, size, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built);
    }

    public Empty(City city) {
        super(false, BuildingType.EMPTY, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_land1_00106.png", 58, 30, 1, new ArrayList<>(), 0, 0, 0, 0, 1, 1, 0f, city, false);

    }

    @Override
    public boolean build(int xPos, int yPos) {
        return true;
    }

    @Override
    public BufferedImage getRender() {
        return getImg();
    }
}
