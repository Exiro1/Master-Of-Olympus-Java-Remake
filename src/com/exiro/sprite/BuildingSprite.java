package com.exiro.sprite;

import com.exiro.buildingList.Building;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

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
        this.directional = false;
        setImage(0);
    }

    public BuildingSprite(String filePath, int bitID, int localId, int frameNumber, City c, Building b, Direction direction) {
        super(filePath, bitID, localId, frameNumber, c);
        this.building = b;
        this.x = b.getXB();
        this.y = b.getYB();
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

    public void setImage(int frame) {
        TileImage t = getImage(frame);
        currentFrame = makeColorTransparent(t.getImg(), Color.RED);
        height = t.getH();
        width = t.getW();
        setImg(t);
    }


    @Override
    public void process(double deltaTime) {
        if(!visible)
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

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        return null;
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

}
