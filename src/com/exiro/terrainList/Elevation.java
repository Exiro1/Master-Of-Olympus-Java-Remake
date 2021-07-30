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

    public Elevation(int xpos, int ypos, Direction dir, int id, City c, ObjectClass over, int size) {
        super(true, ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true);
        this.id = id;
        this.direction = dir;
        this.over = over;
        this.size = size;
        setRockImg(id, size);
    }

    public void setRockImg(int number, int size) {

        int i = 0;
        if (size == 1) {
            switch (direction) {
                case SUD:
                    i = 28;
                    break;
                case SUD_EST:
                    city.getMap().getCase(this.xPos - 1, this.yPos).getTerrain().setConstructible(false);
                    i = 14;
                    break;
                case EST:
                    //city.getMap().getCase(this.xPos,this.yPos-1).getTerrain().setConstructible(false);
                    i = 27;
                    break;
                case NORD_EST:
                    city.getMap().getCase(this.xPos - 1, this.yPos).getTerrain().setConstructible(true);
                    city.getMap().getCase(this.xPos - 1, this.yPos).getTerrain().setBlocking(false);
                    i = 25;
                    break;
                case NORD:
                    setConstructible(true);
                    i = 26;
                    break;
                case NORD_OUEST:
                    city.getMap().getCase(this.xPos, this.yPos - 1).getTerrain().setConstructible(true);
                    city.getMap().getCase(this.xPos, this.yPos - 1).getTerrain().setBlocking(false);
                    i = 24;
                    break;
                case OUEST:
                    i = 29;
                    break;
                case SUD_OUEST:
                    city.getMap().getCase(this.xPos, this.yPos - 1).getTerrain().setConstructible(false);
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
                    //setConstructible(true);
                    i = 35;
                    break;
                case NORD:
                    setConstructible(true);
                    i = 10;
                    break;
                case NORD_OUEST:
                    //setConstructible(true);
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
        this.setLocalID(number + i);
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

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public void process(double deltaTime) {

    }
}
