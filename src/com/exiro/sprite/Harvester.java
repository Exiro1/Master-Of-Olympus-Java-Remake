package com.exiro.sprite;

import com.exiro.buildingList.IndustryHarverster;
import com.exiro.reader.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

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
        setRoutePath(c.getPathManager().getPathTo(c.getMap().getCase(getXB(), getYB()), harvestSite, FreeState.NON_BLOCKING.getI(), false));

    }

    public Direction getDirToSite(Case site) {
        Case last = c.getMap().getCase(getXB(), getYB());
        index++;
        Case next = site;
        int x = last.getxPos() - next.getxPos();
        int y = last.getyPos() - next.getyPos();

        if (x == -1 && y == 0) {
            return Direction.SOUTH_EAST;
        } else if (x == 1 && y == 0) {
            return Direction.NORTH_WEST;
        } else if (x == 0 && y == 1) {
            return Direction.NORTH_EAST;
        } else if (x == 0 && y == -1) {
            return Direction.SOUTH_WEST;
        }
        return Direction.EAST;
    }


    @Override
    public void process(double deltaTime, int deltaDays) {


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
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir()))) {
                setDir(path.next());
            } else if (path.getIndex() == path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) {
                hasArrived = true;
                setX(path.getPath().get(path.getIndex()).getxPos());
                setY(path.getPath().get(path.getIndex()).getyPos());
            }
            if (!hasArrived) {
                if (getDir() == Direction.SOUTH_WEST) {
                    y = y + (float) (speed * deltaTime);
                    x = Math.round(x);
                } else if (getDir() == Direction.NORTH_WEST) {
                    x = x - (float) (speed * deltaTime);
                    y = Math.round(y);
                } else if (getDir() == Direction.NORTH_EAST) {
                    y = y - (float) (speed * deltaTime);
                    x = Math.round(x);
                } else if (getDir() == Direction.SOUTH_EAST) {
                    x = x + (float) (speed * deltaTime);
                    y = Math.round(y);
                } else if (getDir() == Direction.SOUTH) {
                    y = y + (float) (speed * deltaTime * 0.71);
                    x = x + (float) (speed * deltaTime * 0.71);
                } else if (getDir() == Direction.NORTH) {
                    y = y - (float) (speed * deltaTime * 0.71);
                    x = x - (float) (speed * deltaTime * 0.71);
                } else if (getDir() == Direction.EAST) {
                    y = y - (float) (speed * deltaTime * 0.71);
                    x = x + (float) (speed * deltaTime * 0.71);
                } else if (getDir() == Direction.WEST) {
                    y = y + (float) (speed * deltaTime * 0.71);
                    x = x - (float) (speed * deltaTime * 0.71);
                }
            }
            setXB((int) Math.ceil(x));
            setYB((int) Math.ceil(y));
            setMainCase(c.getMap().getCase(getXB(), getYB()));
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


    public void harvestFinished() {
        setDestination(industry);
        loaded = true;
        harvesting = false;
        hasArrived = false;
        setRoutePath(c.getPathManager().getPathTo(c.getMap().getCase(getXB(), getYB()), industry.getAccess().get(0), FreeState.NON_BLOCKING.getI(), false));
    }

    @Override
    public void delete() {
        super.delete();
    }


    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
