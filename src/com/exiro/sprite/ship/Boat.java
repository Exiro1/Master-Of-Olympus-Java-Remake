package com.exiro.sprite.ship;

import com.exiro.depacking.TileImage;
import com.exiro.object.BaseObject;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.Map;

public class Boat extends MovingSprite {


    public Boat(String filePath, int bitID, int localId, int frameNumber, City c, BaseObject destination) {
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

            }
            setImage(getDir(), index);
        }
        if (path != null && !hasArrived) {
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) /*|| (getDir() == Direction.EAST && path.getIndex() < path.getPath().size() - 1)*/) {
                setDir(path.next());
            } else if (path.getIndex() == path.getPath().size() - 1) {
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
            } else if (getDir() == Direction.SOUTH) {
                y = y + (float) (speed * deltaTime);
                x = x + (float) (speed * deltaTime);
            } else if (getDir() == Direction.NORTH) {
                y = y - (float) (speed * deltaTime);
                x = x - (float) (speed * deltaTime);
            } else if (getDir() == Direction.EAST) {
                y = y - (float) (speed * deltaTime);
                x = x + (float) (speed * deltaTime);
            } else if (getDir() == Direction.WEST) {
                y = y + (float) (speed * deltaTime);
                x = x - (float) (speed * deltaTime);
            }

            setXB((int) Math.ceil(x));
            setYB((int) Math.ceil(y));
            setMainCase(c.getMap().getCase((int) (getXB()), (int) (getYB())));
        }

    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }


}
