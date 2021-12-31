package com.exiro.sprite;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.*;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Carter extends MovingSprite {

    BufferedImage cart;
    int cartH, cartW;
    int cartOffX, cartOffY;
    final Resource res;
    int amount;
    int prio = 0;
    int currentDelivery = 0;
    Building origin;
    int command = 0;

    public Carter(City c, ObjectClass destination, Building origin, Resource resource, int nbr) {
        super("SprMain", 0, 4728, 12, c, destination);
        dir = Direction.EST;
        timeBetweenFrame = 0.05f;
        Case ca = origin.getAccess().get(0);
        x = ca.getxPos();
        y = ca.getyPos();
        offsetX = 0;
        offsetY = -5;
        setXB(Math.round(x));
        setYB(Math.round(y));
        res = resource;
        this.amount = nbr;
        setCart(dir);
        setImage(dir, 0);
        this.origin = origin;
    }
    public Carter(City c, ObjectClass destination, Building origin, Resource resource, int nbr, int command) {
        super("SprMain", 0, 4728, 12, c, destination);
        dir = Direction.EST;
        timeBetweenFrame = 0.05f;
        Case ca = origin.getAccess().get(0);
        x = ca.getxPos();
        y = ca.getyPos();
        offsetX = 0;
        offsetY = -5;
        setXB(Math.round(x));
        setYB(Math.round(y));
        res = resource;
        this.amount = nbr;
        setCart(dir);
        setImage(dir, 0);
        this.origin = origin;
        this.command = command;
    }


    public int getCurrentDelivery() {
        return currentDelivery;
    }

    public void setCurrentDelivery(int currentDelivery) {
        this.currentDelivery = currentDelivery;
    }

    public void setPath(Path path) {
        this.path = path;
        if (path.getPath().size() > 1) {
            setDir(path.next());
        } else {

        }
    }

    public void setCart(Direction direction) {

        int i = 0;
        switch (direction) {

            case SUD:
                i = 3;
                prio = 1;
                cartOffX = -4;
                cartOffY = 9;
                break;
            case SUD_EST:
                offsetX = -5;
                offsetY = -8;
                i = 2;
                prio = 1;
                cartOffX = 3;
                cartOffY = -24;
                break;
            case EST:
                i = 1;
                cartOffX = 10;
                cartOffY = 10;
                prio = 0;
                break;
            case NORD_EST:
                offsetX = 0;
                offsetY = -5;
                i = 0;
                prio = 0;
                cartOffX = 0;
                cartOffY = -38;
                break;
            case NORD:
                i = 7;
                cartOffX = -6;
                cartOffY = -2;
                prio = 0;
                break;
            case NORD_OUEST:
                offsetX = 0;
                offsetY = -5;
                i = 6;
                cartOffX = -39;
                cartOffY = -39;
                prio = 0;
                break;
            case OUEST:
                i = 5;
                cartOffX = -25;
                cartOffY = 10;
                prio = 0;
                break;
            case SUD_OUEST:
                offsetX = 8;
                offsetY = -13;
                i = 4;
                cartOffX = -38;
                cartOffY = -19;
                prio = 1;
                break;
        }
        int resid = resID(res);
        int idnbr = 1;

        if (amount == 1)
            idnbr = 1;
        if (amount == 2)
            idnbr = 2;
        if (amount == 3)
            idnbr = 2;
        if (amount == 4)
            idnbr = 3;

        if ((res == Resource.WOOL || res == Resource.BRONZE || res == Resource.ARMEMENT || res == Resource.OLIVE_OIL || res == Resource.WINE) && amount > 2)
            idnbr = 2;

        int id = resid + i + 8 * (idnbr - 1);

        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), id);
        cart = t.getImg();
        cartH = t.getH();
        cartW = t.getW();
        switch (direction) {

            case SUD:
                cartOffX += 32 - cartW;
                cartOffY += 26 - cartH;
                break;
            case SUD_EST:
                cartOffX += 36 - cartW;
                cartOffY += 30 - cartH;
                break;
            case EST:
                cartOffX += 38 - cartW;
                cartOffY += 22 - cartH;
                break;
            case NORD_EST:
                cartOffX += 36 - cartW;
                cartOffY += 28 - cartH;
                break;
            case NORD:
                cartOffX += 26 - cartW;
                cartOffY += 30 - cartH;
                break;
            case NORD_OUEST:
                cartOffX += 36 - cartW;
                cartOffY += 28 - cartH;
                break;
            case OUEST:
                cartOffX += 38 - cartW;
                cartOffY += 22 - cartH;
                break;
            case SUD_OUEST:
                cartOffX += 36 - cartW;
                cartOffY += 30 - cartH;
                break;
        }

    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        int z = 1;
        if (c.getMap().getCase(getXB(), getYB()) != null)
            z = c.getMap().getCase(getXB(), getYB()).getZlvl();
        Point p = IsometricRender.TwoDToIsoSprite(new Point(getX() - z, (getY()) - z), getWidth(), getHeight());

        if (prio == 0)
            g.drawImage(cart, camX + (int) p.getX() + getOffsetX() + cartOffX, camY + (int) p.getY() + getOffsetY() + cartOffY, null);
        g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), null);
        if (prio == 1)
            g.drawImage(cart, camX + (int) p.getX() + getOffsetX() + cartOffX, camY + (int) p.getY() + getOffsetY() + cartOffY, null);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);

        if (getRoutePath() != null)
            return;
        if(res.getDelivery() != null){
            for (ObjectType type : res.getDelivery()) {
                for (Building b : getC().getBuildingList(type)) {
                    if (b instanceof IndustryConverter) {
                        if (((IndustryConverter) b).roomForInputResources() > 0) {
                            Path p = getC().getPathManager().getPathTo(getXB(), getYB(), b.getAccess().get(0).getxPos(), b.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                            if (p != null) {
                                int delivery = Math.min(((IndustryConverter) b).roomForInputResources(), amount);
                                ((IndustryConverter) b).addIncomming(delivery);
                                currentDelivery = delivery;
                                setPath(p);
                                setDestination(b);
                                return;
                            }
                        }
                    }
                }
            }
        }
        for (Building b : getC().getBuildings()) {
            if (b instanceof StoreBuilding) {
                StoreBuilding g = (StoreBuilding) b;
                if (g.getFreeSpace(res) > 0 && g.getAccess().size() > 0) {
                    Path p = getC().getPathManager().getPathTo(getXB(), getYB(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                    if (p != null) {
                        setPath(p);
                        setDestination(g);
                        currentDelivery = getAmount() - g.reserve(res, getAmount());
                        return;
                    }
                }
            }
        }
    }

    public int resID(Resource r) {
        int i = 0;
        if(amount == 0)
            return 8427;
        switch (r) {
            case SEA_URCHIN:
                i = 8435;
                break;
            case FISH:
                i = 8459;
                break;
            case MEAT:
                i = 8483;
                break;
            case CHEESE:
                i = 8507;
                break;
            case CARROT:
                i = 8531;
                break;
            case ONION:
                i = 8555;
                break;
            case BRONZE:
                i = 8603;
                break;
            case GRAPE:
                i = 8619;
                break;
            case OLIVE:
                i = 8643;
                break;
            case WOOL:
                i = 8667;
                break;
            case ARMEMENT:
                i = 8683;
                break;
            case OLIVE_OIL:
                i = 8699;
                break;
            case WINE:
                i = 8715;
                break;
            case CORN:
                i = 8579;
                break;
            case NULL:
                i = 8427;
                break;
            default:
                i = 8427;
                break;
        }
        return i;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        return false;
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    public int getCommand() {
        return command;
    }

    @Override
    public void setDir(Direction dir) {
        super.setDir(dir);
        setCart(dir);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
