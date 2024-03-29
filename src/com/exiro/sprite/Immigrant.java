package com.exiro.sprite;

import com.exiro.reader.TileImage;
import com.exiro.moveRelated.Path;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;

import java.util.Map;

public class Immigrant extends MovingSprite {

    private static Map<Direction, TileImage[]> spriteSet;
    //private static int frameNumber = 12, size = 15, width = 64, height = 64, marge = 3;
    //private static String filepath = "SprMain";
    private int nbr;

    public Immigrant(City city, Path p, ObjectClass dest, int nbr) {
        super("SprMain", 0, 1792, 12, city, dest);
        x = p.getStart().getxPos();
        y = p.getStart().getyPos();
        this.nbr = nbr;
        path = p;
        offsetX = 0;
        offsetY = 0;
        timeBetweenFrame = 0.05f;
    }


    @Override
    public void delete() {
        super.delete();
        this.hasArrived = true;
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
