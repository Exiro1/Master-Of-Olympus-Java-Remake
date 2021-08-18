package com.exiro.sprite;

import com.exiro.buildingList.IndustryHarverster;
import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.util.ArrayList;
import java.util.Map;

public class Harvester extends MovingSprite {


    protected boolean harvesting = false;
    boolean loaded = false;
    Case harvestSite;
    int daysUntilFinish;
    int lastDay;
    IndustryHarverster industry;
    boolean finish = false;

    public Harvester(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination, Case harvestSite, int harvestTime, IndustryHarverster industry) {
        super(filePath, bitID, localId, frameNumber, c, destination);
        this.harvestSite = harvestSite;
        daysUntilFinish = harvestTime;
        this.industry = industry;
        Case start = industry.getAccess().get(0);
        x = start.getxPos();
        y = start.getyPos();
        setXB(Math.round(x));
        setYB(Math.round(y));
        setRoutePath(c.getPathManager().getPathTo(c.getMap().getCase(getXB(), getYB()), harvestSite, FreeState.WALKABLE.getI()));

    }

    public Direction getDirToSite(Case site) {
        Case last = c.getMap().getCase(getXB(), getYB());
        index++;
        Case next = site;
        int x = last.getxPos() - next.getxPos();
        int y = last.getyPos() - next.getyPos();

        if (x == -1 && y == 0) {
            return Direction.SUD_EST;
        } else if (x == 1 && y == 0) {
            return Direction.NORD_OUEST;
        } else if (x == 0 && y == 1) {
            return Direction.NORD_EST;
        } else if (x == 0 && y == -1) {
            return Direction.SUD_OUEST;
        }
        return Direction.EST;
    }


    @Override
    public void process(double deltaTime) {


        timeSinceLastFrame = timeSinceLastFrame + deltaTime;
        // System.out.println(timeSinceLastFrame);
        if (timeSinceLastFrame > timeBetweenFrame) {
            index++;
            timeSinceLastFrame = 0;
            if (index >= frameNumber) {
                index = 0;

            }
            setImage(getDir(), index);
        }
        if (path != null && !hasArrived) {
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) || (getDir() == Direction.EST && path.getIndex() < path.getPath().size() - 1)) {
                setDir(path.next());
            } else if (path.getIndex() == path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) {
                hasArrived = true;
            }
            if (getDir() == Direction.SUD_OUEST) {
                y = y + (float) (speed * deltaTime);
            } else if (getDir() == Direction.NORD_OUEST) {
                x = x - (float) (speed * deltaTime);
            } else if (getDir() == Direction.NORD_EST) {
                y = y - (float) (speed * deltaTime);
            } else if (getDir() == Direction.SUD_EST) {
                x = x + (float) (speed * deltaTime);
            }
            setXB(Math.round(x));
            setYB(Math.round(y));
        }

        if (harvesting) {
            if (GameManager.getInstance().getTimeManager().getDay() != lastDay) {
                lastDay = GameManager.getInstance().getTimeManager().getDay();
                daysUntilFinish--;
                if (daysUntilFinish <= 0) {
                    harvestFinished();
                }
            }
        } else if (!loaded) {
            if (hasArrived) {
                harvesting = true;
                hasArrived = false;
                arrivedToSite();
            }
        } else if (loaded && hasArrived) {
            finish = true;
        }

    }

    public boolean finished() {
        return finish;
    }

    public void arrivedToSite() {

    }

    ;


    public void harvestFinished() {
        setDestination(industry);
        loaded = true;
        harvesting = false;
        hasArrived = false;
        setRoutePath(c.getPathManager().getPathTo(c.getMap().getCase(getXB(), getYB()), industry.getAccess().get(0), FreeState.WALKABLE.getI()));
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
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
