package com.exiro.Sprite;

import com.exiro.FileManager.ImageLoader;
import com.exiro.MoveRelated.Path;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Immigrant extends Sprite {

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

    static public void loadSprite() {
        spriteSet = new HashMap<>();
        spriteSet.put(Direction.EST, new TileImage[frameNumber]);
        spriteSet.put(Direction.NORD, new TileImage[frameNumber]);
        spriteSet.put(Direction.SUD, new TileImage[frameNumber]);
        spriteSet.put(Direction.OUEST, new TileImage[frameNumber]);
        spriteSet.put(Direction.NORD_EST, new TileImage[frameNumber]);
        spriteSet.put(Direction.NORD_OUEST, new TileImage[frameNumber]);
        spriteSet.put(Direction.SUD_EST, new TileImage[frameNumber]);
        spriteSet.put(Direction.SUD_OUEST, new TileImage[frameNumber]);


        int nbr = 0;
        int k = 0;

        for (int a = 0; a < 8 * frameNumber; a++) {
            Direction dir = null;
            k = a % 8;
            nbr = (int) (a / 8.0);
            switch (k) {
                case 1:
                    dir = Direction.EST;
                    break;
                case 7:
                    dir = Direction.NORD;
                    break;
                case 0:
                    dir = Direction.NORD_EST;
                    break;
                case 6:
                    dir = Direction.NORD_OUEST;
                    break;
                case 5:
                    dir = Direction.OUEST;
                    break;
                case 3:
                    dir = Direction.SUD;
                    break;
                case 2:
                    dir = Direction.SUD_EST;
                    break;
                case 4:
                    dir = Direction.SUD_OUEST;
                    break;

            }

            TileImage t = ImageLoader.getImage(filepath, 0, 1792 + a);
            t.setImg(makeColorTransparent(t.getImg(), Color.RED));

            spriteSet.get(dir)[nbr] = t;


        }


    }
/*
    static public void loadSprite2() {
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
*/

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
