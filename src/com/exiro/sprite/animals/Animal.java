package com.exiro.sprite.animals;

import com.exiro.reader.TileImage;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.Map;

public class Animal extends MovingSprite {


    public Animal(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination) {
        super(filePath, bitID, localId, frameNumber, c, destination);
        setComplex(true);
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
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) || (getDir() == Direction.EAST && path.getIndex() < path.getPath().size() - 1)) {
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
            }
        } else if (hasArrived) {
            y = Math.round(y);
            x = Math.round(x);
        }
        setXB((int) Math.ceil(x));
        setYB((int) Math.ceil(y));
        setMainCase(c.getMap().getCase(getXB(), getYB()));

    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }


}
