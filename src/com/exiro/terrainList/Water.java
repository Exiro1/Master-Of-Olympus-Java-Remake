package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.animals.Fish;
import com.exiro.utils.Point;

import java.awt.*;

public class Water extends Terrain {

    int frameNumber = 8;
    int index = 0;
    double timeSinceLastFrame = 0;
    double timeBetweenFrame = 0.1f;
    Fish fish;

    public Water(int xpos, int ypos, City city) {
        super(ObjectType.WATERTERRAIN, false, xpos, ypos, city, true, false, true, 1);
        city.addTerrain(this);
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        timeSinceLastFrame += deltaTime;
        if (timeSinceLastFrame > timeBetweenFrame) {
            timeSinceLastFrame = 0;
            index++;
            if (index >= frameNumber) {
                index = 0;
            }
            this.setLocalID(getType().getLocalID() + index);
            updateImg();
        }

    }


    @Override
    public void Render(Graphics g, int camX, int camY) {
        int lvl = getMainCase().getZlvl();
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - lvl, getyPos() - lvl), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        if (fish != null) {
            fish.Render(g, camX, camY);
        }

    }


}
