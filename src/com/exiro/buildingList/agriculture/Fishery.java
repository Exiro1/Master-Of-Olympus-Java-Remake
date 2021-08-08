package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.Direction;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.WaterCoast;

import java.util.ArrayList;

public class Fishery extends ResourceGenerator {

    //9508 pecheur dans l eau
    //10796 bateau pecheur
    Direction direction;
    double growth = 0;
    int speedFactor = 1;

    public Fishery(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    public Fishery(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.FISHERY, BuildingCategory.FOOD, pop, 10, 50, 10, xPos, yPos, 2, 2, cases, built, city, 0, Resource.FISH);
    }

    public Fishery() {
        super(false, ObjectType.FISHERY, BuildingCategory.FOOD, 0, 10, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.FISH);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            int i = 0;
            int idSprite = 0;
            switch (direction) {
                case SUD_EST:
                    i = 1;
                    idSprite = 949;
                    break;
                case NORD_EST:
                    i = 0;
                    idSprite = 909;
                    break;
                case NORD_OUEST:
                    i = 3;
                    idSprite = 889;
                    break;
                case SUD_OUEST:
                    i = 2;
                    idSprite = 929;
                    break;
            }
            setLocalID(getBuildingType().getLocalID() + i);
            updateImg();
/*
            BuildingSprite s = new BuildingSprite("SprAmbient", 0, idSprite, 20, getCity(), this);
            s.setOffsetX(44);
            s.setOffsetY(6);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
*/

            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();

        Case c1 = city.getMap().getCase(xPos, yPos);
        Case c2 = city.getMap().getCase(xPos + 1, yPos);
        Case c3 = city.getMap().getCase(xPos, yPos - 1);
        Case c4 = city.getMap().getCase(xPos + 1, yPos - 1);

        if (!c1.isOccuped() && !c1.getTerrain().getBuildingType().isBlocking()
                && !c2.isOccuped() && !c2.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccuped() && (c3.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c4.isOccuped() && (c4.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORD_EST && ((WaterCoast) c4.getTerrain()).getDirection() == Direction.NORD_EST) {
                direction = Direction.NORD_EST;
                place.add(c1);
                place.add(c2);
                place.add(c3);
                place.add(c4);
            }
        } else if (!c3.isOccuped() && !c3.getTerrain().getBuildingType().isBlocking()
                && !c4.isOccuped() && !c4.getTerrain().getBuildingType().isBlocking()
                && !c1.isOccuped() && (c1.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c2.isOccuped() && (c2.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.SUD_OUEST && ((WaterCoast) c2.getTerrain()).getDirection() == Direction.SUD_OUEST) {
                direction = Direction.SUD_OUEST;
                place.add(c3);
                place.add(c4);
                place.add(c1);
                place.add(c2);
            }
        } else if (!c2.isOccuped() && !c2.getTerrain().getBuildingType().isBlocking()
                && !c4.isOccuped() && !c4.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccuped() && (c3.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c1.isOccuped() && (c1.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.NORD_OUEST && ((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORD_OUEST) {
                direction = Direction.NORD_OUEST;
                place.add(c2);
                place.add(c4);
                place.add(c1);
                place.add(c3);
            }
        } else if (!c1.isOccuped() && !c1.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccuped() && !c3.getTerrain().getBuildingType().isBlocking()
                && !c2.isOccuped() && (c2.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c4.isOccuped() && (c4.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c2.getTerrain()).getDirection() == Direction.SUD_EST && ((WaterCoast) c4.getTerrain()).getDirection() == Direction.SUD_EST) {
                direction = Direction.SUD_EST;
                place.add(c1);
                place.add(c3);
                place.add(c2);
                place.add(c4);
            }
        }

        return place;
    }


    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            growth += factor * deltaTime * speedFactor;
            if (growth > 60) {
                resourceCreated(1);
            }

        }
    }

    @Override
    protected void addPopulation() {

    }
}
