package com.exiro.sprite.delivery.carter;

import com.exiro.reader.TileImage;
import com.exiro.moveRelated.Path;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.Map;

public class TrolleyDriver extends MovingSprite {
    boolean start = false;
    ComplexCarter cc;

    public TrolleyDriver(City c, ObjectClass destination, int xpos, int ypos, ComplexCarter cc) {
        super("SprMain", 0, 1336, 12, c, destination);
        this.setX(xpos);
        this.setY(ypos);
        setXB(Math.round(xpos));
        setYB(Math.round(ypos));
        this.cc = cc;
        start = true;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        if (path != null && !hasArrived && start) {
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

            setXB((int) Math.ceil(x));
            setYB((int) Math.ceil(y));
            setMainCase(c.getMap().getCase(getXB(), getYB()));
        }


    }

    public ComplexCarter getCc() {
        return cc;
    }

    public void setPath(Path path) {
        this.path = path;
        if (path.getPath().size() > 1) {
            setDir(path.next());
        } else {

        }
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}

