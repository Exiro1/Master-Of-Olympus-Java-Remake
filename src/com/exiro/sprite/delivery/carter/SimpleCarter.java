package com.exiro.sprite.delivery.carter;

import com.exiro.buildingList.Building;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.Direction;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class SimpleCarter extends Carter {

    BufferedImage cart;
    int cartH, cartW;
    int cartOffX, cartOffY;

    int prio = 0;



    public SimpleCarter(City c, ObjectClass destination, Building origin, Resource resource, int amount,int command,int currentDelivery) {
        super("SprMain", 0, 4728, 12, c, destination, resource, amount, command, currentDelivery, origin);
        dir = Direction.EAST;
        timeBetweenFrame = 0.05f;
        Case ca = origin.getAccess().get(0);
        x = ca.getxPos();
        y = ca.getyPos();
        offsetX = 0;
        offsetY = -5;
        setXB(Math.round(x));
        setYB(Math.round(y));
        setCart(dir);
        setImage(dir, 0);
        setMainCase(ca);
    }

    public void setPath(Path path) {
        this.path = path;
        if (path.getPath().size() > 1) {
            setDir(path.next());
        } else {

        }
    }

    @Override
    public void updateImg() {
        super.updateImg();
        setCart(dir);
    }

    public void setCart(Direction direction) {

        int i = 0;
        switch (direction) {

            case SOUTH:
                i = 3;
                prio = 1;
                cartOffX = -4;
                cartOffY = 9;
                break;
            case SOUTH_EAST:
                offsetX = -5;
                offsetY = -8;
                i = 2;
                prio = 1;
                cartOffX = 3;
                cartOffY = -24;
                break;
            case EAST:
                i = 1;
                cartOffX = 10;
                cartOffY = 10;
                prio = 0;
                break;
            case NORTH_EAST:
                offsetX = 0;
                offsetY = -5;
                i = 0;
                prio = 0;
                cartOffX = 0;
                cartOffY = -38;
                break;
            case NORTH:
                i = 7;
                cartOffX = -6;
                cartOffY = -2;
                prio = 0;
                break;
            case NORTH_WEST:
                offsetX = 0;
                offsetY = -5;
                i = 6;
                cartOffX = -39;
                cartOffY = -39;
                prio = 0;
                break;
            case WEST:
                i = 5;
                cartOffX = -25;
                cartOffY = 10;
                prio = 0;
                break;
            case SOUTH_WEST:
                offsetX = 8;
                offsetY = -13;
                i = 4;
                cartOffX = -38;
                cartOffY = -19;
                prio = 1;
                break;
        }
        int resid = resID(resource);
        int idnbr = 1;

        if (amount == 1)
            idnbr = 1;
        if (amount == 2)
            idnbr = 2;
        if (amount == 3)
            idnbr = 2;
        if (amount == 4)
            idnbr = 3;

        if ((resource == Resource.WOOL || resource == Resource.BRONZE || resource == Resource.ARMEMENT || resource == Resource.OLIVE_OIL || resource == Resource.WINE) && amount > 2)
            idnbr = 2;

        int id = resid + i + 8 * (idnbr - 1);

        TileImage t = ImageLoader.getImage(getPath(), getBitmapID(), id);
        cart = t.getImg();
        cartH = t.getH();
        cartW = t.getW();
        switch (direction) {

            case SOUTH:
                cartOffX += 32 - cartW;
                cartOffY += 26 - cartH;
                break;
            case SOUTH_EAST:
                cartOffX += 36 - cartW;
                cartOffY += 30 - cartH;
                break;
            case EAST:
                cartOffX += 38 - cartW;
                cartOffY += 22 - cartH;
                break;
            case NORTH_EAST:
                cartOffX += 36 - cartW;
                cartOffY += 28 - cartH;
                break;
            case NORTH:
                cartOffX += 26 - cartW;
                cartOffY += 30 - cartH;
                break;
            case NORTH_WEST:
                cartOffX += 36 - cartW;
                cartOffY += 28 - cartH;
                break;
            case WEST:
                cartOffX += 38 - cartW;
                cartOffY += 22 - cartH;
                break;
            case SOUTH_WEST:
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
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
    }

    @Override
    public void setArrived(boolean hasarrived) {
        this.hasArrived = hasarrived;
    }
    public boolean hasArrived(){
        return hasArrived;
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
    public void delete() {
        super.delete();
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

}
