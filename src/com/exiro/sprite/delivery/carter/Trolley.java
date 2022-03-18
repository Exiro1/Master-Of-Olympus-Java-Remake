package com.exiro.sprite.delivery.carter;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.Path;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.Map;

public class Trolley extends MovingSprite {


    boolean start = false;
    ComplexCarter cc;

    private int cartOffX;
    private int cartOffY;

    public Trolley(City c, ObjectClass destination, int xpos, int ypos, ComplexCarter cc) {
        super("SprMain", 0, 2990, 1, c, destination);
        this.setX(xpos);
        this.setY(ypos);
        setXB(Math.round(xpos));
        setYB(Math.round(ypos));
        this.cc = cc;
        setImage(getDir(),0);
    }

    public void setImage(Direction direction, int frame) {
        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), getID(direction));
        currentFrame = makeColorTransparent(t.getImg(), Color.RED);
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    public int getID(Direction direction) {
        if(cc == null)
            return 0;
        int i = 0;
        switch (direction) {

            case SOUTH:
                i = 3;
                cartOffX = -4;
                cartOffY = 9;
                break;
            case SOUTH_EAST:
                i = 2;
                cartOffX = 5;
                cartOffY = 5;
                break;
            case EAST:
                i = 1;
                cartOffX = 10;
                cartOffY = 10;
                break;
            case NORTH_EAST:
                i = 0;
                cartOffX = 0;
                cartOffY = 0;
                break;
            case NORTH:
                i = 7;
                cartOffX = -6;
                cartOffY = -2;
                break;
            case NORTH_WEST:
                i = 6;
                cartOffX = -17;
                cartOffY = 1;
                break;
            case WEST:
                i = 5;
                cartOffX = -25;
                cartOffY = 10;
                break;
            case SOUTH_WEST:
                i = 4;
                cartOffX = -26;
                cartOffY = 10;
                break;
        }
        int resid = resID(cc.resource);
        int idnbr = 1;
        if (cc.amount == 1)
            idnbr = 1;
        if (cc.amount == 2)
            idnbr = 1;
        if (cc.amount == 3)
            idnbr = 2;
        if (cc.amount == 4)
            idnbr = 2;

        return resid + i + 8 * (idnbr - 1);
    }


    @Override
    public void setDir(Direction dir) {
        super.setDir(dir);
        setImage(dir, 0);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        if (!start) {
            if (Math.abs(cc.puller.getX() - getX()) > 1 || Math.abs(cc.puller.getY() - getY()) > 1)
                start = true;
        }
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

    public void setPath(Path path) {
        this.path = path;
        if (path.getPath().size() > 1) {
            setDir(path.next());
        } else {

        }
    }

    public int resID(Resource r) {
        int i = 0;
        if(cc == null)
            return 2990;

        if(cc.amount == 0)
            return 2990;
        if (r == null)
            return 2990;
        switch (r) {
            case MARBLE:
                i = 3014;
                break;
            case SCULPTURE:
                i = 3030;
                break;
            case WOOD:
                i = 2998;
                break;
            case NULL:
                i = 2990;
                break;
            default:
                i = 2990;
                break;
        }
        return i;
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