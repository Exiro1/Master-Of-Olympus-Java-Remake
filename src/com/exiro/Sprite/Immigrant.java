package com.exiro.Sprite;

import com.exiro.MoveRelated.Path;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Immigrant extends Sprite {

    private static Map<Direction, BufferedImage[]> spriteSet;
    private static int frameNumber = 12, size = 15, width = 64, height = 64, marge = 3;
    private static String filepath = "Assets/Sprites/Immigrant/immigrant.png";
    private int nbr;


    public Immigrant(City city, Path p, ObjectClass dest, int nbr) {
        super(filepath, 15, 12, 64, 64, 3, city, dest);
        x = 0;
        y = 0;
        this.nbr = nbr;
        path = p;
    }

    static public void loadSprite() {
        spriteSet = new HashMap<>();
        spriteSet.put(Direction.EST, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.NORD, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.SUD, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.OUEST, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.NORD_EST, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.NORD_OUEST, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.SUD_EST, new BufferedImage[frameNumber]);
        spriteSet.put(Direction.SUD_OUEST, new BufferedImage[frameNumber]);

        int i = -1;
        int j = 0;
        int k = 0;
        File f = new File(filepath);
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int nbr = -1;
        int k2 = 0;
        for (int a = 0; a < 8 * frameNumber; a++) {
            i++;
            if (i > size - 1) {
                i = 0;
                j++;
            }
            nbr++;
            if (nbr > frameNumber - 1) {
                k++;
                nbr = 0;
            }

            Direction dir = null;
            switch (k) {
                case 0:
                    dir = Direction.EST;
                    break;
                case 1:
                    dir = Direction.NORD;
                    break;
                case 2:
                    dir = Direction.NORD_EST;
                    break;
                case 3:
                    dir = Direction.NORD_OUEST;
                    break;
                case 4:
                    dir = Direction.OUEST;
                    break;
                case 5:
                    dir = Direction.SUD;
                    break;
                case 6:
                    dir = Direction.SUD_EST;
                    break;
                case 7:
                    dir = Direction.SUD_OUEST;
                    break;

            }

            spriteSet.get(dir)[nbr] = makeColorTransparent(img.getSubimage(i * (width + marge), j * (height + marge), width, height), Color.RED);


        }


    }

    @Override
    public boolean build(int xPos, int yPos) {
        return false;
    }

    @Override
    public BufferedImage getRender() {
        return this.currentFrame;
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
    public Map<Direction, BufferedImage[]> getSpriteSet() {
        return spriteSet;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }
}
