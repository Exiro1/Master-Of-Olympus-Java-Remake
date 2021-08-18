package com.exiro.sprite.complexCarter;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Trolley extends MovingSprite {


    boolean start = false;
    ComplexCarter cc;
    Resource resource;
    int amount;
    int currentDelivery = 0;
    private int cartOffX;
    private int cartOffY;

    public Trolley(City c, ObjectClass destination, int xpos, int ypos, ComplexCarter cc, Resource resource, int amount) {
        super("SprMain", 0, 2990, 1, c, destination);
        this.setX(xpos);
        this.setY(ypos);
        setXB(Math.round(xpos));
        setYB(Math.round(ypos));
        this.cc = cc;
        this.resource = resource;
        this.amount = amount;
        setImage(getDir(), 0);

    }

    public void setImage(Direction direction, int frame) {


        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), getID(direction));

        currentFrame = makeColorTransparent(t.getImg(), Color.RED);
        height = t.getH();
        width = t.getW();
        setImg(t);
    }

    public int getID(Direction direction) {

        int i = 0;
        switch (direction) {

            case SUD:
                i = 3;
                cartOffX = -4;
                cartOffY = 9;
                break;
            case SUD_EST:
                i = 2;
                cartOffX = 5;
                cartOffY = 5;
                break;
            case EST:
                i = 1;
                cartOffX = 10;
                cartOffY = 10;
                break;
            case NORD_EST:
                i = 0;
                cartOffX = 0;
                cartOffY = 0;
                break;
            case NORD:
                i = 7;
                cartOffX = -6;
                cartOffY = -2;
                break;
            case NORD_OUEST:
                i = 6;
                cartOffX = -17;
                cartOffY = 1;
                break;
            case OUEST:
                i = 5;
                cartOffX = -25;
                cartOffY = 10;
                break;
            case SUD_OUEST:
                i = 4;
                cartOffX = -26;
                cartOffY = 10;
                break;
        }
        int resid = resID(resource);
        int idnbr = 0;
        if (amount == 1)
            idnbr = 1;
        if (amount == 2)
            idnbr = 1;
        if (amount == 3)
            idnbr = 2;
        if (amount == 4)
            idnbr = 2;

        return resid + i + 8 * (idnbr - 1);
    }


    @Override
    public void setDir(Direction dir) {
        super.setDir(dir);
        setImage(dir, 0);
    }

    @Override
    public void process(double deltaTime) {
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

        if (getRoutePath() != null)
            return;

        for (Building b : getC().getBuildings()) {
            if (b instanceof StoreBuilding) {
                StoreBuilding g = (StoreBuilding) b;
                if (g.getFreeSpace(resource) > 0 && g.getAccess().size() > 0) {
                    Path p = getC().getPathManager().getPathTo(getXB(), getYB(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                    if (p != null) {
                        setPath(p);
                        cc.getDriver().setPath(p);
                        cc.getPuller().setPath(p);
                        cc.setDest(g);
                        currentDelivery = amount - g.reserve(resource, amount);
                        return;
                    }
                }

            }
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCurrentDelivery() {
        return currentDelivery;
    }

    public void setCurrentDelivery(int currentDelivery) {
        this.currentDelivery = currentDelivery;
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