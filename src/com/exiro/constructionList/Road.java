package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Road extends Construction {


    boolean start = false;

    public Road(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, boolean isFloor) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, isFloor);
    }

    public Road(City city) {
        super(false, ObjectType.ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, true);
    }

    public Road() {
        super(false, ObjectType.ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, true);
    }

    @Override
    public void process(double deltatime) {

    }


    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean isBuilt = super.build(xPos, yPos);
        if (isBuilt) {//change en route bloqué
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
    public void delete() {
        if (!this.start) {
            super.delete();
            city.getPathManager().deleteRoad(this);
        }
    }

    public void setBlockableRoad() {
        setType(ObjectType.ROAD);
    }


}



