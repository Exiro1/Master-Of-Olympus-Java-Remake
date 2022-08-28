package com.exiro.sprite.animals;

import com.exiro.reader.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.render.IsometricRender;
import com.exiro.terrainList.Water;
import com.exiro.utils.Point;

import java.awt.*;

public class Fish extends Animal {


    public Fish(City c, Case origin) {
        super("SprAmbient", 0, 452, 40, c, null);
        timeBetweenFrame = 0.15f;
        setMainCase(origin);
        setX(origin.getxPos());
        setY(origin.getyPos());
        setXB(origin.getxPos());
        setYB(origin.getyPos());
        setOffsetY(-30);
        setComplex(true);
        if (origin.getTerrain() instanceof Water) {
            ((Water) origin.getTerrain()).setFish(this);
        }
    }

    @Override
    public void setMainCase(Case mainCase) {
        if (mainCase == this.mainCase || mainCase == null)
            return;
        this.mainCase = mainCase;
    }


    public TileImage getImage(int frame) {
        return ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + frame);
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
        if (frameNumber > 1) {
            timeSinceLastFrame = timeSinceLastFrame + deltaTime;
            // System.out.println(timeSinceLastFrame);
            if (timeSinceLastFrame > timeBetweenFrame) {
                index++;
                timeSinceLastFrame = 0;
                if (index >= frameNumber) {
                    index = 0;
                }
                setImage(index);
            }
        }

    }

    @Override
    public void Render(Graphics g, int camX, int camY) {

        int z = c.getMap().getCase(getXB(), getYB()).getZlvl();

        if (complex) {
            com.exiro.utils.Point p = IsometricRender.TwoDToIsoSprite(new com.exiro.utils.Point(getX() - z, (getY()) - z), getWidth(), getHeight());

            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), null);
        } else {
            com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point((getX() - z), (getY() - z)), getWidth(), getHeight(), 1); //isoTexture parce que c'est celui que j'utilisait avant l implémentation de isoSprite et j'ai la fleme de refaire les offsets

            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), null);
        }
    }

}
