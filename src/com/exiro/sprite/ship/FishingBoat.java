package com.exiro.sprite.ship;

import com.exiro.buildingList.agriculture.Fishery;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.BaseObject;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Time;

public class FishingBoat extends Boat {

    BoatState boatState;
    Fishery origin;
    Case start;
    int imgFactor = 2;
    Time startFishing;

    public FishingBoat(City c, BaseObject destination, Fishery origin) {
        super("SprMain", 0, 10796, 4, c, destination);
        start = origin.getSpawnPoint();
        boatState = BoatState.TRAVEL;
        x = start.getxPos();
        y = start.getyPos();
        this.setRoutePath(c.getPathManager().getPathTo(origin.getSpawnPoint(), destination.getMainCase(), FreeState.WATER.getI()));
        setXB(Math.round(x));
        setYB(Math.round(y));
        this.origin = origin;
    }

    @Override
    public void setImage(Direction direction, int frame) {

        int i = 0;
        switch (direction) {

            case SOUTH:
                i = 3;
                break;
            case SOUTH_EAST:
                i = 2;
                break;
            case EAST:
                i = 1;
                break;
            case NORTH_EAST:
                i = 0;
                break;
            case NORTH:
                i = 7;
                break;
            case NORTH_WEST:
                i = 6;
                break;
            case WEST:
                i = 5;
                break;
            case SOUTH_WEST:
                i = 4;
                break;
        }
        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + imgFactor * i + frame * 8 * imgFactor);

        currentFrame = t.getImg();
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    public void goFishing(BaseObject destination) {
        hasArrived = false;
        setState(BoatState.TRAVEL);
        setDestination(destination);
        this.setRoutePath(c.getPathManager().getPathTo(origin.getSpawnPoint(), destination.getMainCase(), FreeState.WATER.getI()));
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        if (boatState == BoatState.FISHING) {
            if (GameManager.getInstance().getTimeManager().daysSince(startFishing) > 15) {
                setState(BoatState.TRAVEL);
                hasArrived = false;
                this.setRoutePath(c.getPathManager().getPathTo(getMainCase(), start, FreeState.WATER.getI()));
                destination = origin;
            }
        } else if (boatState != BoatState.WAITING) {
            if (hasArrived && destination == origin) {
                setState(BoatState.WAITING);
                origin.fishingFinished();
            } else if (hasArrived) {
                setState(BoatState.FISHING);
                startFishing = GameManager.getInstance().getTimeManager().getTime();
            }
        }
    }

    public void setState(BoatState state) {
        this.boatState = state;
        switch (state) {

            case TRAVEL:
                setLocalID(10796);
                setFrameNumber(4);
                imgFactor = 2;
                break;
            case FISHING:
                setLocalID(10859);
                setFrameNumber(10);
                imgFactor = 1;
                break;
            case WAITING:
                setLocalID(10939);
                setFrameNumber(1);
                imgFactor = 1;
                break;
        }
    }

    enum BoatState {
        TRAVEL, WAITING, FISHING;
    }

}
