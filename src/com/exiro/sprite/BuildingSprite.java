package com.exiro.sprite;

import com.exiro.buildingList.Building;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;

import java.awt.*;
import java.util.ArrayList;

public class BuildingSprite extends Sprite {

    final Building building;

    public BuildingSprite(String filePath, int bitID, int localId, int frameNumber, City c, Building b) {
        super(filePath, bitID, localId, frameNumber, c);
        this.building = b;
        this.x = b.getXB();
        this.y = b.getYB();
        setImage(0);
    }

    public TileImage getImage(int frame) {
        return ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + frame);
    }

    public void setImage(int frame) {
        TileImage t = getImage(frame);
        currentFrame = makeColorTransparent(t.getImg(), Color.RED);
        ;
        height = t.getH();
        width = t.getW();
        setImg(t);
    }


    @Override
    public void process(double deltaTime) {

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
