package com.exiro.constructionList;

import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class OliveTree extends Construction {

    int growLevel = 0;
    float growth = 0;

    public OliveTree(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, int growLevel) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, false);
        this.growLevel = growLevel;
    }

    public OliveTree(City city) {
        super(false, ObjectType.OLIVETREE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, false);
    }

    public OliveTree() {
        super(false, ObjectType.OLIVETREE, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, false);
    }

    @Override
    public void process(double deltatime) {
        growth += deltatime;
        if (growth > 3) {
            growth = 0;
            if (growLevel < 5) {
                growLevel++;
                setImg(ImageLoader.getImage(getBuildingType().getPath(), getBitmapID(), getLocalID() + growLevel));
            }
        }
    }


}
