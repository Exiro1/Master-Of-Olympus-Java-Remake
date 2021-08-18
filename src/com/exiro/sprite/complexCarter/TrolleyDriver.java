package com.exiro.sprite.complexCarter;

import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.util.ArrayList;
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
    public void process(double deltaTime) {
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
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir())) || (getDir() == Direction.EST && path.getIndex() < path.getPath().size() - 1)) {
                setDir(path.next());
            } else if (path.getIndex() == path.getPath().size() - 1) {
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

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}

