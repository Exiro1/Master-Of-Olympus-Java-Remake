package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.*;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.animals.Fish;
import com.exiro.sprite.ship.FishingBoat;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Water;
import com.exiro.terrainList.WaterCoast;
import com.exiro.utils.Time;

import java.util.ArrayList;
import java.util.Random;

public class Fishery extends ResourceGenerator {

    //9508 pecheur dans l eau
    //10796 bateau pecheur
    static Random random = new Random();
    Direction direction;
    double growth = 0;
    int speedFactor = 1;
    Case spawnPoint;
    FishingBoat boat = null;

    public Fishery() {
        super(false, ObjectType.FISHERY, BuildingCategory.FOOD, 0, 10, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.FISH, 2);
        maxPerCarter = 1;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargements de " + getResource().getName(), 16, 20, 80);

        return bi;
    }

    int idSprite;
    Fish closest;
    Time start;
    boolean boatFishing = false;
    boolean boatBuilt = false;
    boolean boatStarted = false;

    public Case getSpawn() {
        return getSpawn(getxPos(), getyPos());
    }

    public Case getSpawn(int x, int y) {
        for (int i = 0; i < 2 + 2; i++) {
            for (int j = 0; j < 2 + 2; j++) {
                if (!((i == 0 && j == 0) || (i == 0 && j == 2 + 1) || (i == 2 + 1 && j == 0) || (i == 2 + 1 && j == 2 + 1))) {
                    Case c = city.getMap().getCase(x + i - 1, y - j + 1);
                    if (c.getTerrain() instanceof Water) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            int i = 0;
            idSprite = 0;
            int offx = 0, offy = 0;

            switch (direction) {
                case SOUTH_EAST:
                    i = 1;
                    idSprite = 949;
                    offx = 41;
                    offy = -17;
                    break;
                case NORTH_EAST:
                    i = 0;
                    idSprite = 909;
                    offx = 48;
                    offy = -33;
                    break;
                case NORTH_WEST:
                    i = 3;
                    idSprite = 889;
                    offx = 14;
                    offy = -30;
                    break;
                case SOUTH_WEST:
                    i = 2;
                    idSprite = 929;
                    offx = 10;
                    offy = -16;
                    break;
            }
            setLocalID(getBuildingType().getLocalID() + i);
            updateImg();
            spawnPoint = getSpawn();
            BuildingSprite s = new BuildingSprite("SprAmbient", 0, idSprite, 20, getCity(), this);
            s.setOffsetX(offx);
            s.setOffsetY(offy);
            s.setTimeBetweenFrame(0.1f);
            s.setComplex(true);
            s.setVisible(false);
            addSprite(s);


            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof FishingBoat) {
                if (ms.hasArrived && ms.getDestination() == this) {
                    //toR.add(ms);
                }
            }
        }
        for (MovingSprite ms : toR) {
            removeSprites(ms);
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
            if (((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORTH_EAST && ((WaterCoast) c4.getTerrain()).getDirection() == Direction.NORTH_EAST) {
                direction = Direction.NORTH_EAST;
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
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.SOUTH_WEST && ((WaterCoast) c2.getTerrain()).getDirection() == Direction.SOUTH_WEST) {
                direction = Direction.SOUTH_WEST;
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
            if (((WaterCoast) c1.getTerrain()).getDirection() == Direction.NORTH_WEST && ((WaterCoast) c3.getTerrain()).getDirection() == Direction.NORTH_WEST) {
                direction = Direction.NORTH_WEST;
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
            if (((WaterCoast) c2.getTerrain()).getDirection() == Direction.SOUTH_EAST && ((WaterCoast) c4.getTerrain()).getDirection() == Direction.SOUTH_EAST) {
                direction = Direction.SOUTH_EAST;
                place.add(c1);
                place.add(c3);
                place.add(c2);
                place.add(c4);
            }
        }
        for (Fish f : city.getResourceManager().getFishes()) {
            Path p = city.getPathManager().getPathTo(getSpawn(xPos, yPos), f.getMainCase(), FreeState.WATER.getI());
            if (p != null) {
                return place;
            }

        }
        place.clear();
        return place;
    }

    public void createBoat() {
        start = GameManager.getInstance().getTimeManager().getTime();
        int min = 10000;
        int d = 0;
        setBuilding();
        for (Fish f : city.getResourceManager().getFishes()) {
            Path p = city.getPathManager().getPathTo(this.getSpawnPoint(), f.getMainCase(), FreeState.WATER.getI());
            if (p == null)
                continue;
            d = p.getPath().size();
            if (d < min) {
                min = d;
                closest = f;
            }
        }

    }

    public BaseObject getFishingPlace() {
        int index = closest.getMainCase().getNeighbourCount(FreeState.WATER.getI(), city.getMap());
        return closest.getMainCase().getNeighbourIndex(FreeState.WATER.getI(), random.nextInt(index), city.getMap()).getTerrain();
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            if (!boatStarted) {
                createBoat();
                boatStarted = true;
            }
            if (!boatBuilt && GameManager.getInstance().getTimeManager().daysSince(start) > 15) {
                boatBuilt = true;
                FishingBoat p = new FishingBoat(city, getFishingPlace(), this);
                boatFishing = true;
                addSprite(p);
                boat = p;
                setWaiting();
            }
            if (!boatFishing && GameManager.getInstance().getTimeManager().daysSince(start) > 15) {
                boat.goFishing(getFishingPlace());
                boatFishing = true;
                setWaiting();
                resourceCreated(1);
            }
        }
    }

    public void fishingFinished() {
        boatFishing = false;
        start = GameManager.getInstance().getTimeManager().getTime();
        setUnloading();
    }

    public void setBuilding() {

        int id = 0;
        int offx = 0, offy = 0;
        switch (direction) {
            case SOUTH_EAST:
                id = 981;
                offx = 41;
                offy = -17;
                break;
            case NORTH_EAST:
                id = 991;
                offx = 53;
                offy = -20;
                break;
            case NORTH_WEST:
                id = 981;
                offx = 25;
                offy = -28;
                break;
            case SOUTH_WEST:
                id = 991;
                offx = 15;
                offy = -25;
                break;
        }
        BuildingSprite s = getBuildingSprites().get(0);
        s.setVisible(true);
        s.setOffsetX(offx);
        s.setOffsetY(offy);
        s.setLocalID(id);
        s.setFrameNumber(10);
        s.setDirection(Direction.NORTH_EAST);
        s.setDirectional(false);
    }

    public void setUnloading() {
        int offx = 0, offy = 0;

        switch (direction) {
            case SOUTH_EAST:
                idSprite = 949;
                offx = 41;
                offy = -17;
                break;
            case NORTH_EAST:
                idSprite = 909;
                offx = 48;
                offy = -33;
                break;
            case NORTH_WEST:
                idSprite = 889;
                offx = 14;
                offy = -30;
                break;
            case SOUTH_WEST:
                idSprite = 929;
                offx = 10;
                offy = -16;
                break;
        }
        BuildingSprite s = getBuildingSprites().get(0);
        s.setLocalID(idSprite);
        s.setOffsetX(offx);
        s.setOffsetY(offy);
        s.setFrameNumber(20);
        s.setDirection(Direction.NORTH_EAST);
        s.setDirectional(false);
    }

    public void setWaiting() {
        int offx = 0, offy = 0;
        switch (direction) {
            case SOUTH_EAST:
                offx = 47;
                offy = 0;
                break;
            case NORTH_EAST:
                offx = 40;
                offy = -35;
                break;
            case NORTH_WEST:
                offx = 5;
                offy = -30;
                break;
            case SOUTH_WEST:
                offx = 0;
                offy = 0;
                break;
        }
        BuildingSprite s = getBuildingSprites().get(0);
        s.setOffsetX(offx);
        s.setOffsetY(offy);
        s.setLocalID(2116);
        s.setFrameNumber(12);
        s.setDirection(direction);
        s.setDirectional(true);

        /*s.setOffsetX(offx);
        s.setOffsetY(offy);
        s.setTimeBetweenFrame(0.1f);

         */
    }


    @Override
    public void stop() {
        super.stop();
        boat = null;
    }

    @Override
    protected void addPopulation() {

    }

    public Case getSpawnPoint() {
        return spawnPoint;
    }
}
