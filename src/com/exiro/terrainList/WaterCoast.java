package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;
import com.exiro.terrainGenerator.CoastType;

import java.util.ArrayList;


public class WaterCoast extends Terrain {

    boolean angle;
    Direction direction;
    int number;

    public WaterCoast(int xpos, int ypos, boolean angle, Direction direction, int number, City city) {
        super(true, ObjectType.WATERTCOAST, false, xpos, ypos, city, true, false, true);
        this.angle = angle;
        this.direction = direction;
        this.number = number;
        this.setLocalID(getIDfromDir(direction, number));
        updateImg();

    }

    public WaterCoast(int xpos, int ypos, CoastType type, int nbr,City city) {
        super(true, ObjectType.WATERTCOAST, false, xpos, ypos, city, true, false, true);
        this.setLocalID(type.getId()+Math.min(type.getNbr()-1,nbr));
        direction = type.getDir();
        updateImg();
    }

    public int getIDfromDir(Direction dir, int number) {
        int i = 0;
        switch (dir) {
            case SUD:
                i = 191;
                break;
            case SUD_EST:
                i = 175;
                break;
            case EST:
                i = 187;
                break;
            case NORD_EST:
                i = 171;
                break;
            case NORD:
                i = 199;
                break;
            case NORD_OUEST:
                i = 183;
                break;
            case OUEST:
                i = 195;
                break;
            case SUD_OUEST:
                i = 179;
                break;
        }
        i += number;
        return i;
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
    public void process(double deltaTime) {

    }

    public Direction getDirection() {
        return direction;
    }
}
