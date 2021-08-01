package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class BlockingRoad extends Construction {


    public BlockingRoad(City city) {
        super(true, ObjectType.BLOCKABLE_ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, true);
    }

    public BlockingRoad() {
        super(true, ObjectType.BLOCKABLE_ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, true);
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    Case c = city.getMap().getCase(xPos + j, yPos - i);
                    if (c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    @Override
    public void process(double deltatime) {

    }

}
