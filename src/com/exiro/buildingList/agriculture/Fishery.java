package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Direction;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.WaterCoast;

import java.util.ArrayList;

public class Fishery extends ResourceGenerator {

    //9508 pecheur dans l eau
    //10796 bateau pecheur
    Direction direction;
    double growth = 0;
    int speedFactor = 1;


    public Fishery() {
        super(false, ObjectType.FISHERY, BuildingCategory.FOOD, 0, 10, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.FISH,2);
        maxPerCarter = 1;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargements de " + getResource().getName(), 16, 20, 80);

        return bi;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            int i = 0;
            int idSprite = 0;
            int offx = 0, offy = 0;

            switch (direction) {
                case SUD_EST:
                    i = 1;
                    idSprite = 949;
                    offx = 41;
                    offy = -17;
                    break;
                case NORD_EST:
                    i = 0;
                    idSprite = 909;
                    offx = 48;
                    offy = -33;
                    break;
                case NORD_OUEST:
                    i = 3;
                    idSprite = 889;
                    offx = 14;
                    offy = -30;
                    break;
                case SUD_OUEST:
                    i = 2;
                    idSprite = 929;
                    offx = 10;
                    offy = -16;
                    break;
            }
            setLocalID(getBuildingType().getLocalID() + i);
            updateImg();

            BuildingSprite s = new BuildingSprite("SprAmbient", 0, idSprite, 20, getCity(), this);
            s.setOffsetX(offx);
            s.setOffsetY(offy);
            s.setTimeBetweenFrame(0.1f);
            s.setComplex(true);
            addSprite(s);

            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            s.process(delta);
        }
    }



    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();

        Case c1 = city.getMap().getCase(xPos, yPos);
        Case c2 = city.getMap().getCase(xPos + 1, yPos);
        Case c3 = city.getMap().getCase(xPos, yPos - 1);
        Case c4 = city.getMap().getCase(xPos + 1, yPos - 1);
        if(c1 == null || c2 == null|| c3 == null|| c4 == null )
            return place;
        if (!c1.isOccupied() && !c1.getTerrain().getBuildingType().isBlocking()
                && !c2.isOccupied() && !c2.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccupied() && (c3.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c4.isOccupied() && (c4.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORD_EST && ((WaterCoast) c4.getTerrain()).getDirection() == Direction.NORD_EST) {
                direction = Direction.NORD_EST;
                place.add(c1);
                place.add(c2);
                place.add(c3);
                place.add(c4);
            }
        } else if (!c3.isOccupied() && !c3.getTerrain().getBuildingType().isBlocking()
                && !c4.isOccupied() && !c4.getTerrain().getBuildingType().isBlocking()
                && !c1.isOccupied() && (c1.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c2.isOccupied() && (c2.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.SUD_OUEST && ((WaterCoast) c2.getTerrain()).getDirection() == Direction.SUD_OUEST) {
                direction = Direction.SUD_OUEST;
                place.add(c3);
                place.add(c4);
                place.add(c1);
                place.add(c2);
            }
        } else if (!c2.isOccupied() && !c2.getTerrain().getBuildingType().isBlocking()
                && !c4.isOccupied() && !c4.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccupied() && (c3.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c1.isOccupied() && (c1.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
        ) {
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.NORD_OUEST && ((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORD_OUEST) {
                direction = Direction.NORD_OUEST;
                place.add(c2);
                place.add(c4);
                place.add(c1);
                place.add(c3);
            }
        } else if (!c1.isOccupied() && !c1.getTerrain().getBuildingType().isBlocking()
                && !c3.isOccupied() && !c3.getTerrain().getBuildingType().isBlocking()
                && !c2.isOccupied() && (c2.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
                && !c4.isOccupied() && (c4.getTerrain().getBuildingType() == ObjectType.WATERTCOAST)
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
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            growth += factor * deltaTime * speedFactor;
            if (growth > 60) {
                resourceCreated(1);
                growth = 0;
            }

        }
    }

    @Override
    protected void addPopulation() {

    }
}
