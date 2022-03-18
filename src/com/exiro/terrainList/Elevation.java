package com.exiro.terrainList;

import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;
import com.exiro.terrainGenerator.ElevationType;

import java.awt.*;

public class Elevation extends Terrain {

    int id;
    Direction direction;
    ObjectClass over;
    int size;
    boolean roadPossible = false;
    boolean hasRoad = false;

    public Elevation(int xpos, int ypos, Direction dir, int id, City c, ObjectClass over, int size) {
        super(ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true, size);
        this.id = id;
        this.direction = dir;
        this.over = over;
        this.size = size;
        setRockImg(id, size);
    }

    public Elevation(int xpos, int ypos, Direction dir, int id, City c, ObjectClass over, int size, boolean roadPossible) {
        super(ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true, size);
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

    public Elevation(int xpos, int ypos, ElevationType type, City c, int size, boolean roadPossible, int nbr) {
        super(ObjectType.ELEVATION, false, xpos, ypos, c, false, false, true, size);
        this.id = id;
        this.direction = type.getDir();
        this.size = size;
        this.setBitmapID(5);
        this.setLocalID(type.getId());
        this.roadPossible = roadPossible;
        this.setConstructible(roadPossible);
        this.setBlocking(!roadPossible);

        setRockImg(type,nbr);
    }

    public void setHasRoad(boolean hasRoad) {
        this.hasRoad = hasRoad;
        setRockImg(this.id, this.size);
    }

    public int getSizeZ() {
        return size;
    }


    public void setRockImg(ElevationType type, int nbr) {
        if(type == ElevationType.E4){ //sud est
            if(nbr == 0){
                setBitmapID(5);
                setLocalID(type.getId());
            }else{
                setBitmapID(7);
                setLocalID(13+nbr);
            }
        }else if(type == ElevationType.E6){ // sud ouest
            if(nbr == 0){
                setBitmapID(5);
                setLocalID(type.getId());
            }else{
                setBitmapID(7);
                setLocalID(16+nbr);
            }
        }else if(type == ElevationType.E5){  //sud est-ouest
            if(nbr == 0){
                setBitmapID(5);
                setLocalID(type.getId());
            }else{
                setBitmapID(7);
                setLocalID(28);
            }
        }




        /*else if(type == ElevationType.E13 || type == ElevationType.E14 || type == ElevationType.E15 || type == ElevationType.E16){
            if(nbr == 0){
                setBitmapID(1);
                setLocalID(376);
            }else{
                setBitmapID(7);
                setLocalID(16+nbr);
            }
        }*/else{
            setLocalID(type.getId());
        }

        if(roadPossible){
            nbr = 0;
            this.setBitmapID(5);
            switch (direction) {
                case SOUTH_EAST:
                    setLocalID(33);
                    break;
                case NORTH_EAST:
                    setLocalID(32);
                    break;
                case NORTH_WEST:
                    setLocalID(35);
                    break;
                case SOUTH_WEST:
                    setLocalID(34);
                    break;
            }
        }

        updateImg();
    }

    public void setRockImg(int number, int size) {
        if (roadPossible && size == 1)
            this.setBitmapID(5);
        int i = 0;
        if (size == 1) {
            switch (direction) {
                case SOUTH:
                    i = 28;
                    break;
                case SOUTH_EAST:
                    if (roadPossible) {
                        i = 33;
                        break;
                    }
                    i = 14;
                    break;
                case EAST:
                    i = 27;
                    break;
                case NORTH_EAST:
                    if (roadPossible) {
                        i = 32;
                        break;
                    }
                    i = 25;
                    break;
                case NORTH:
                    i = 26;
                    break;
                case NORTH_WEST:
                    if (roadPossible) {
                        i = 35;
                        break;
                    }
                    i = 24;
                    break;
                case WEST:
                    i = 29;
                    break;
                case SOUTH_WEST:
                    if (roadPossible) {
                        i = 34;
                        break;
                    }
                    i = 17;
                    break;
            }
        } else {
            switch (direction) {
                case SOUTH:
                    i = 12;
                    break;
                case SOUTH_EAST:
                    i = 0;
                    break;
                case EAST:
                    i = 11;
                    break;
                case NORTH_EAST:
                    i = 35;
                    break;
                case NORTH:
                    i = 10;
                    break;
                case NORTH_WEST:
                    i = 34;
                    break;
                case WEST:
                    i = 13;
                    break;
                case SOUTH_WEST:
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


    public boolean isRoadPossible() {
        return roadPossible;
    }

    public boolean isHasRoad() {
        return hasRoad;
    }


    @Override
    public void process(double deltaTime, int deltaDays) {

    }
}
