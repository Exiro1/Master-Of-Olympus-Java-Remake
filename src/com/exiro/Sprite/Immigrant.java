package com.exiro.Sprite;

import com.exiro.MoveRelated.Path;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.depacking.TileImage;

import java.util.ArrayList;
import java.util.Map;

public class Immigrant extends MovingSprite {

    private static Map<Direction, TileImage[]> spriteSet;
    private static int frameNumber = 12, size = 15, width = 64, height = 64, marge = 3;
    private static String filepath = "SprMain";
    private int nbr;

    public Immigrant(City city, Path p, ObjectClass dest, int nbr) {
        super(filepath, 0, 1792, 12, city, dest);
        x = 0;
        y = 0;
        this.nbr = nbr;
        path = p;
        offsetX = 0;
        offsetY = 0;
    }




    @Override
    public boolean build(int xPos, int yPos) {
        return false;
    }

    @Override
    public void delete() {
        this.hasArrived = true;
    }

    @Override
    public ArrayList<Case> getAccess() {
        return new ArrayList<>();
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return spriteSet;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }
}
