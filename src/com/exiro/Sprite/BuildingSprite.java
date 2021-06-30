package com.exiro.Sprite;

import com.exiro.BuildingList.Building;
import com.exiro.FileManager.ImageLoader;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.depacking.TileImage;

import java.util.ArrayList;

public class BuildingSprite extends Sprite {

    Building building;

    public BuildingSprite(String filePath, int bitID, int localId, int frameNumber, City c, Building b) {
        super(filePath, bitID, localId, frameNumber, c);
        this.building = b;
        this.x = b.getXB();
        this.y = b.getYB();
    }

    public TileImage getImage(int frame) {
        return ImageLoader.getImage(getPath(), getBitmapID(), getLocalID() + frame);
    }

    public void setImage(int frame) {
        TileImage t = getImage(frame);
        currentFrame = t.getImg();
        height = t.getH();
        width = t.getW();
    }


    @Override
    public void process(double deltaTime) {
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

        //setXB((int) Math.round(x));
        //setYB((int) Math.round(y));
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
