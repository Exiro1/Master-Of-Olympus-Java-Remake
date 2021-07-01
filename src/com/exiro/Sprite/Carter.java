package com.exiro.Sprite;

import com.exiro.BuildingList.Building;
import com.exiro.BuildingList.Granary;
import com.exiro.BuildingList.Stock;
import com.exiro.FileManager.ImageLoader;
import com.exiro.MoveRelated.Path;
import com.exiro.MoveRelated.RoadMap;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Object.Ressource;
import com.exiro.Render.IsometricRender;
import com.exiro.Utils.Point;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Carter extends MovingSprite {

    BufferedImage cart;
    int cartH, cartW;
    int cartOffX, cartOffY;
    Ressource res;
    int ammount;
    int prio = 0;
    int currentDelivery = 0;

    public Carter(City c, ObjectClass destination, Building origin, Ressource ressource, int nbr) {
        super("SprMain", 0, 4728, 12, c, destination);
        dir = Direction.NORD;
        timeBetweenFrame = 0.1f;
        Case ca = origin.getAccess().get(0);
        x = ca.getxPos();
        y = ca.getyPos();
        offsetX = 0;
        offsetY = -5;
        setXB((int) Math.round(x));
        setYB((int) Math.round(y));
        res = ressource;
        if ((res == Ressource.WOOL || res == Ressource.BRONZE || res == Ressource.ARMEMENT || res == Ressource.OLIVE_OIL || res == Ressource.WINE) && nbr > 2)
            nbr = 2;
        if (res == Ressource.NULL)
            nbr = 1;
        this.ammount = nbr;
        setCart(dir);
        setImage(dir, 0);
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
                i = 2;
                prio = 1;
                cartOffX = 5;
                cartOffY = 5;
                break;
            case EST:
                i = 1;
                cartOffX = 10;
                cartOffY = 10;
                prio = 0;
                break;
            case NORD_EST:
                i = 0;
                prio = 0;
                cartOffX = 0;
                cartOffY = 0;
                break;
            case NORD:
                i = 7;
                cartOffX = -6;
                cartOffY = -2;
                prio = 0;
                break;
            case NORD_OUEST:
                i = 6;
                cartOffX = -17;
                cartOffY = 1;
                prio = 0;
                break;
            case OUEST:
                i = 5;
                cartOffX = -25;
                cartOffY = 10;
                prio = 0;
                break;
            case SUD_OUEST:
                i = 4;
                cartOffX = -26;
                cartOffY = 10;
                prio = 1;
                break;
        }
        int resid = resID(res);
        int idnbr = 0;
        if (ammount == 1)
            idnbr = 1;
        if (ammount == 2)
            idnbr = 2;
        if (ammount == 3)
            idnbr = 2;
        if (ammount == 4)
            idnbr = 3;

        int id = resid + i + 8 * (idnbr - 1);

        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), id);
        cart = makeColorTransparent(t.getImg(), Color.RED);
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
        com.exiro.Utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getX(), (getY())), getWidth(), getHeight(), 1);
        if (prio == 0)
            g.drawImage(cart, camX + (int) p.getX() + getOffsetX() + cartOffX, camY + (int) p.getY() + getOffsetY() + cartOffY, null);
        g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), null);
        if (prio == 1)
            g.drawImage(cart, camX + (int) p.getX() + getOffsetX() + cartOffX, camY + (int) p.getY() + getOffsetY() + cartOffY, null);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);

        if (getRoutePath() != null)
            return;

        for (Building b : getC().getBuildings()) {
            if (b instanceof Granary) {
                Granary g = (Granary) b;
                if (g.getFreeSpace(res) > 0 && g.getAccess().size() > 0) {
                    Path p = getC().getPathManager().getPathTo(getXB(), getYB(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), RoadMap.FreeState.ALL_ROAD);
                    if (p != null) {
                        setPath(p);
                        setDestination(g);
                        currentDelivery = getAmmount() - g.reserve(res, getAmmount());
                    }
                }
            } else if (b instanceof Stock) {
                Stock g = (Stock) b;
                if (g.getFreeSpace(res) > 0 && g.getAccess().size() > 0) {
                    Path p = getC().getPathManager().getPathTo(getXB(), getYB(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), RoadMap.FreeState.ALL_ROAD);
                    if (p != null) {
                        setPath(p);
                        setDestination(g);
                        currentDelivery = getAmmount() - g.reserve(res, getAmmount());
                    }
                }

            }
        }
    }

    public int resID(Ressource r) {
        int i = 0;
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

    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    @Override
    public void setDir(Direction dir) {
        super.setDir(dir);
        setCart(dir);
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }
}
