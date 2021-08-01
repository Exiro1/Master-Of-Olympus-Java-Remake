package com.exiro.sprite;

import com.exiro.ai.DeliveryAI;
import com.exiro.buildingList.House;
import com.exiro.depacking.TileImage;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;

import java.util.ArrayList;
import java.util.Map;

public class WaterDelivery extends DeliverySprite {


    DeliveryAI ai;
    double waterAdd = 10.0;
    int lastY, lastX;

    public WaterDelivery(City c, ObjectClass destination, Case start) {
        super("SprMain", 0, 6736, 12, c, destination, start, 30);
        setTimeBetweenFrame(0.1);
    }

    @Override
    public void deliverBuildings() {
        Case c = getC().getMap().getCase(lastX, lastY);
        for (Case n : c.getNeighbour()) {
            if (n != null && n.getObject() != null && n.getObject().getBuildingType() == ObjectType.HOUSE) {
                ((House) n.getObject()).addWater(waterAdd);
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

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
