package com.exiro.sprite;

import com.exiro.reader.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.Path;
import com.exiro.object.BaseObject;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.utils.Point;

import java.util.Map;

public abstract class MovingSprite extends Sprite {

    public boolean hasArrived = false;
    protected float speed = 2.5f;
    protected Direction dir = Direction.EAST;
    protected Path path;
    protected BaseObject destination;
    protected Path originPath;

    public MovingSprite(String filePath, int bitID, int localId, int frameNumber, City c, BaseObject destination) {
        super(filePath, bitID, localId, frameNumber, c);
        this.destination = destination;
        setImage(dir, 0);
        timeBetweenFrame = 0.05f;
        complex = true;
        //setMainCase(c.getMap().getCase(getXB(), getYB()));
    }

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
        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + i + frame * 8);

        currentFrame = t.getImg();
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
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
            if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), getDir()))) {
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

            setXB((int) Math.ceil(x));
            setYB((int) Math.ceil(y));
            // Register on the tile the sprite is drawn with. ceil() picks, for every direction,
            // the front-most of the tiles the sprite straddles: the destination when moving
            // towards the camera, the origin when moving away. Before, only the four axis moves
            // updated the tile (the diagonal ones never did, so the sprite kept the depth of the
            // tile it started on), and SOUTH_EAST / SOUTH_WEST looked ahead by the sprite's
            // pixel size converted to tiles, about two tiles for a walker.
            setMainCase(c.getMap().getCase(getXB(), getYB()));
        }

    }

    @Override
    public void setMainCase(Case mainCase) {
        if (mainCase == this.mainCase || mainCase == null)
            return;

        // Keep the sprite on exactly one tile. This used to keep the previous tile as well,
        // so the sprite was drawn twice per frame, once at each depth.
        while (onWalking.size() >= size) {
            onWalking.get(0).getSprites().remove(this);
            onWalking.remove(0);
        }
        onWalking.add(mainCase);
        this.mainCase = mainCase;
        mainCase.getSprites().add(this);
    }

    public Case getCaseDir() {
        int i = 0;
        switch (dir) {
            case SOUTH_EAST:
                i = 1;
                break;
            case NORTH_EAST:
                i = 0;
                break;
            case NORTH_WEST:
                i = 3;
                break;
            case SOUTH_WEST:
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

    public BaseObject getDestination() {
        return destination;
    }

    public void setRoutePath(Path path) {
        if(path == null && originPath != null)
            path = originPath.getReversePath();
        this.path = path;
        setDir(path.next());
        if(originPath == null)
            originPath = path;

    }

    public void setDestination(BaseObject destination) {
        this.destination = destination;
    }

    public Path getRoutePath() {
        return path;
    }
}

