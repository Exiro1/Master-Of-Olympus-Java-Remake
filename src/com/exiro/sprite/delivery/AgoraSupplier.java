package com.exiro.sprite.delivery;

import com.exiro.depacking.TileImage;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;

import java.util.ArrayList;
import java.util.Map;

public class AgoraSupplier extends MovingSprite {

    Resource resource;
    int amount;

    public AgoraSupplier(City c, ObjectClass destination, Case origin, Resource resource, int amount) {
        super("SprMain", 0, 1232, 12, c, destination);
        setTimeBetweenFrame(0.1f);
        Case ca = origin;
        setX(ca.getxPos());
        setY(ca.getyPos());
        setXB(Math.round(getX()));
        setYB(Math.round(getY()));
        this.resource = resource;
        this.amount = amount;
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