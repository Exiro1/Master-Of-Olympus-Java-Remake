package com.exiro.sprite;

import com.exiro.buildingList.Building;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.City;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;

public class BuildingSprite extends Sprite {

    final Building building;
    boolean directional;
    Direction direction;
    boolean visible = true;

    public BuildingSprite(String filePath, int bitID, int localId, int frameNumber, City c, Building b) {
        super(filePath, bitID, localId, frameNumber, c);
        this.building = b;
        this.x = b.getXB();
        this.y = b.getYB();
        this.setXB((int) x);
        this.setYB((int) y);
        this.directional = false;
        setImage(0);
    }

    public BuildingSprite(String filePath, int bitID, int localId, int frameNumber, City c, Building b, Direction direction) {
        super(filePath, bitID, localId, frameNumber, c);
        this.building = b;
        this.x = b.getXB();
        this.y = b.getYB();
        this.setXB((int) x);
        this.setYB((int) y);
        this.directional = true;
        this.direction = direction;
        setImage(0);
    }

    public TileImage getImage(int frame) {
        return ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + frame);
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

    public void setImage(int frame) {
        TileImage t = getImage(frame);
        currentFrame = makeColorTransparent(t.getImg(), Color.RED);
        height = t.getH();
        width = t.getW();
        setImg(t);
    }


    @Override
    public void process(double deltaTime, int deltaDays) {
        if (!visible)
            return;
        if (frameNumber > 1) {
            timeSinceLastFrame = timeSinceLastFrame + deltaTime;
            // System.out.println(timeSinceLastFrame);
            if (timeSinceLastFrame > timeBetweenFrame) {
                index++;
                timeSinceLastFrame = 0;
                if (index >= frameNumber) {
                    index = 0;

                }
                if (directional) {
                    setImage(direction, index);
                } else {
                    setImage(index);
                }
            }
        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {

        if(!visible)
            return;

        int z = c.getMap().getCase(getXB(), getYB()).getZlvl();

        if (complex) {
            Point p = IsometricRender.TwoDToIsoSprite(new Point(getX() - z, (getY()) - z), getWidth(), getHeight());
            if (DEBUG) {
                g.setColor(Color.RED);
                g.drawRect(camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), getWidth(), getHeight());
                g.drawString(getXB() + " " + getYB() + " " + getX() + " " + getY(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy());
                g.setColor(Color.BLACK);
            }
            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), null);
        } else {
            Point p = IsometricRender.TwoDToIsoTexture(new Point((getX() - z), (getY() - z)), getWidth(), getHeight(), 1); //isoTexture parce que c'est celui que j'utilisait avant l implémentation de isoSprite et j'ai la fleme de refaire les offsets
            if (DEBUG) {
                g.setColor(Color.RED);
                g.drawRect(camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), getWidth(), getHeight());
                g.drawString(getXB() + " " + getYB(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY());
                g.setColor(Color.BLACK);
            }

            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), null);
        }


    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDirectional(boolean directional) {
        this.directional = directional;
    }

    @Override
    public void delete() {

    }


}
