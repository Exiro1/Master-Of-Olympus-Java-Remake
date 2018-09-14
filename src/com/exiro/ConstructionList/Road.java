package com.exiro.ConstructionList;

import com.exiro.BuildingList.BuildingType;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.SystemCore.GameManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Road extends Construction {


    boolean start = false;

    public Road(boolean isActive, BuildingType type, String path, int width, int height, int size, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built) {
        super(isActive, type, path, width, height, size, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built);
    }

    public Road(City city) {
        super(false, BuildingType.ROAD, "C:\\Users\\cgene\\OneDrive - ESTACA\\Documents\\dev\\JavaGame\\GameJava\\src\\other\\Zeus_Beautification2_00056.png", 58, 30, 1, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false);
    }

    public static Road DEFAULT() {
        return new Road(GameManager.currentCity);
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        super.build(xPos, yPos);
        if (built) {//change en route bloqué
            city.getPathManager().addRoad(this);
            if (getAccess().size() == 0) {
                city.getInActives().add(this);
                setActive(false);
            } else {
                setActive(true);
            }
        } else {
            //setType(BuildingType.BLOCKABLE_ROAD);
        }
        return true;
    }

    @Override
    public BufferedImage getRender() {
        return getImg();
    }

    @Override
    public void delete() {
        if (!this.start) {
            super.delete();
            city.getPathManager().deleteRoad(this);
        }
    }

    public void setBlockableRoad() {
        setType(BuildingType.ROAD);
    }


}




