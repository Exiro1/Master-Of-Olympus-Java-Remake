package com.exiro.sprite.agriculture;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.BaseObject;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.Map;

public class AgricultureSprite extends MovingSprite {


    protected int fullAnimCounter = 0;
    protected boolean unidir = false;
    boolean finished = false;
    boolean createRessource = false;


    public AgricultureSprite(String filePath, int bitID, int localId, int frameNumber, City c, BaseObject destination) {
        super(filePath, bitID, localId, frameNumber, c, destination);
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
                fullAnimCounter++;
            }
            setImage(getDir(), index, unidir);
        }
        if (path != null && !hasArrived) {
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) || (getDir() == Direction.EAST && path.getIndex() < path.getPath().size() - 1)) {
                setDir(path.next());
            } else if (path.getIndex() == path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) {
                hasArrived = true;
            }
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
            }
        } else if (hasArrived) {
            y = Math.round(y);
            x = Math.round(x);
        }
        setXB((int) Math.ceil(x));
        setYB((int) Math.ceil(y));
        setMainCase(c.getMap().getCase(getXB(), getYB()));

    }

    public void setImage(Direction direction, int frame, boolean unidir) {

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
        TileImage t;
        if (!unidir) {
            t = ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + i + frame * 8);
        } else {
            t = ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + frame);
        }
        currentFrame = t.getImg();
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
