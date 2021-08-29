package com.exiro.sprite.animals;

import com.exiro.depacking.TileImage;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.ArrayList;
import java.util.Map;

public class Animal extends MovingSprite {


    public Animal(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination) {
        super(filePath, bitID, localId, frameNumber, c, destination);
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
            } else if (path.getIndex() == path.getPath().size() - 1) {
                hasArrived = true;
            }
            if (getDir() == Direction.SUD_OUEST) {
                y = y + (float) (speed * deltaTime);
                x = Math.round(x);
            } else if (getDir() == Direction.NORD_OUEST) {
                x = x - (float) (speed * deltaTime);
                y = Math.round(y);
            } else if (getDir() == Direction.NORD_EST) {
                y = y - (float) (speed * deltaTime);
                x = Math.round(x);
            } else if (getDir() == Direction.SUD_EST) {
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
    public boolean build(int xPos, int yPos) {
        return false;
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
