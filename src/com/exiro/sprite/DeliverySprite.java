package com.exiro.sprite;

import com.exiro.ai.DeliveryAI;
import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;

import java.util.ArrayList;
import java.util.Map;


public abstract class DeliverySprite extends MovingSprite {

    DeliveryAI ai;
    int lastY, lastX;

    public DeliverySprite(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination, Case start, int len) {
        super(filePath, bitID, localId, frameNumber, c, destination);
        ai = new DeliveryAI();
        this.setRoutePath(ai.roaming(c, len, FreeState.ONLY_ACTIVE_ROAD.getI(), start));
        x = start.getxPos();
        y = start.getyPos();
        setXB(Math.round(x));
        setYB(Math.round(y));
    }

    public abstract void deliverBuildings();

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (lastX != getXB() || lastY != getYB()) {
            lastY = getYB();
            lastX = getXB();
            deliverBuildings();
        }
    }

    public void setAI(DeliveryAI ai) {
        this.ai = ai;
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
}
