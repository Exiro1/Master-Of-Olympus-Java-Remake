package com.exiro.sprite;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.utils.Point;

import java.util.Map;

public abstract class MovingSprite extends Sprite {

    public boolean hasArrived = false;
    protected float speed = 2.5f;
    protected Direction dir = Direction.EST;
    protected Path path;
    protected ObjectClass destination;


    public MovingSprite(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination) {
        super(filePath, bitID, localId, frameNumber, c);
        this.destination = destination;
        setImage(dir, 0);
        timeBetweenFrame = 0.05f;
        complex = true;
        setMainCase(c.getMap().getCase(getXB(), getYB()));
    }

    public void setImage(Direction direction, int frame) {

        int i = 0;
        switch (direction) {

            case SUD:
                i = 3;
                break;
            case SUD_EST:
                i = 2;
                break;
            case EST:
                i = 1;
                break;
            case NORD_EST:
                i = 0;
                break;
            case NORD:
                i = 7;
                break;
            case NORD_OUEST:
                i = 6;
                break;
            case OUEST:
                i = 5;
                break;
            case SUD_OUEST:
                i = 4;
                break;
        }
        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + i + frame * 8);

        currentFrame = t.getImg();
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    @Override
    public void process(double deltaTime) {
        if (path != null && !hasArrived) {
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

            setXB((int) Math.ceil(x));
            setYB((int) Math.ceil(y));
            setMainCase(c.getMap().getCase(getXB(), getYB()));
        }

    }

    @Override
    public void setMainCase(Case mainCase) {
        if (mainCase == this.mainCase)
            return;

        onWalking.add(mainCase);
        this.mainCase = mainCase;

        /*
        if(getCaseDir() != null) {
            onWalking.add(getCaseDir());
            getCaseDir().getSprites().add(this);
        }*/

        if (onWalking.size() > size) {
            onWalking.get(0).getSprites().remove(this);
            onWalking.remove(0);
        }
        mainCase.getSprites().add(this);
    }

    public Case getCaseDir() {
        int i = 0;
        switch (dir) {
            case SUD_EST:
                i = 1;
                break;
            case NORD_EST:
                i = 0;
                break;
            case NORD_OUEST:
                i = 3;
                break;
            case SUD_OUEST:
                i = 2;
                break;
        }
        return this.mainCase.getNeighbour()[i];
    }


    abstract public Map<Direction, TileImage[]> getSpriteSet();


    @Override
    public void delete() {
        for (Case c : onWalking) {
            c.getSprites().remove(this);
        }
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public ObjectClass getDestination() {
        return destination;
    }

    public void setRoutePath(Path path) {
        this.path = path;
    }

    public void setDestination(ObjectClass destination) {
        this.destination = destination;
    }

    public Path getRoutePath() {
        return path;
    }
}

