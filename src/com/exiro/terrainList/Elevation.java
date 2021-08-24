package com.exiro.terrainList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;

import java.awt.*;
import java.util.ArrayList;

public class Elevation extends Terrain {

    int id;
    Direction direction;
    ObjectClass over;
    int size;
    boolean roadPossible = false;
    boolean hasRoad = false;

    public Elevation(int xpos, int ypos, Direction dir, int id, City c, ObjectClass over, int size) {
        super(true, ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true);
        this.id = id;
        this.direction = dir;
        this.over = over;
        this.size = size;
        setRockImg(id, size);
    }

    public Elevation(int xpos, int ypos, Direction dir, int id, City c, ObjectClass over, int size, boolean roadPossible) {
        super(true, ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true);
        this.id = id;
        this.direction = dir;
        this.over = over;
        this.size = size;
        if (size > 1)
            roadPossible = false;
        this.roadPossible = roadPossible;
        this.setConstructible(roadPossible);
        this.setBlocking(!roadPossible);
        setRockImg(id, size);

    }

    public void setHasRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
        setRockImg(this.id, this.size);
    }

    public int getSizeZ() {
        return size;
    }

    public void setRockImg(int number, int size) {
        if (roadPossible && size == 1)
            this.setBitmapID(5);
        int i = 0;
        if (size == 1) {
            switch (direction) {
                case SUD:
                    i = 28;
                    break;
                case SUD_EST:
                    if (roadPossible) {
                        i = 33;
                        break;
                    }
                    i = 14;
                    break;
                case EST:
                    i = 27;
                    break;
                case NORD_EST:
                    if (roadPossible) {
                        i = 32;
                        break;
                    }
                    i = 25;
                    break;
                case NORD:
                    i = 26;
                    break;
                case NORD_OUEST:
                    if (roadPossible) {
                        i = 35;
                        break;
                    }
                    i = 24;
                    break;
                case OUEST:
                    i = 29;
                    break;
                case SUD_OUEST:
                    if (roadPossible) {
                        i = 34;
                        break;
                    }
                    i = 17;
                    break;
            }
        } else {
            switch (direction) {
                case SUD:
                    i = 12;
                    break;
                case SUD_EST:
                    i = 0;
                    break;
                case EST:
                    i = 11;
                    break;
                case NORD_EST:
                    i = 35;
                    break;
                case NORD:
                    i = 10;
                    break;
                case NORD_OUEST:
                    i = 34;
                    break;
                case OUEST:
                    i = 13;
                    break;
                case SUD_OUEST:
                    i = 3;
                    break;
            }
        }
        this.setLocalID(number + i + (hasRoad ? 4 : 0));
        updateImg();
    }


    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
        if (over != null) {
            over.Render(g, camX, camY);
        }
    }

    @Override
    public boolean build(int xPos, int yPos) {

        return false;
    }

    @Override
    public void delete() {

    }

    public boolean isRoadPossible() {
        return roadPossible;
    }

    public boolean isHasRoad() {
        return hasRoad;
    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public void process(double deltaTime) {

    }
}
