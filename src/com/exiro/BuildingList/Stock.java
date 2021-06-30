package com.exiro.BuildingList;

import com.exiro.FileManager.ImageLoader;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;
import com.exiro.Render.IsometricRender;
import com.exiro.SystemCore.GameManager;
import com.exiro.Utils.Point;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Stock extends Building {


    static Map<Ressource, TileImage> listImage;
    Map<Ressource, Integer> stock;

    public Stock(boolean isActive, BuildingType type, String path, int size, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID, Map<Ressource, Integer> stock) {
        super(isActive, type, path, size, 3, 22, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        this.stock = stock;
    }


    public Stock(int xPos, int yPos, City c) {
        //super(false, BuildingType.STOCK, "Assets/Building/Stock/stockInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        super(false, BuildingType.STOCK, "Zeus_General", 1, 3, 22, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);


    }

    static public Stock DEFAULT() {
        return new Stock(0, 0, GameManager.currentCity);
    }


    public TileImage getRessourceTile(Ressource res, int nbr) {
        int i = 0;
        switch (res) {

            case SEA_URCHIN:
                i = 0;
                break;
            case FISH:
                i = 4;
                break;
            case MEAT:
                i = 8;
                break;
            case CHEESE:
                i = 12;
                break;
            case CARROT:
                i = 16;
                break;
            case ONION:
                i = 20;
                break;
            case CORN:
                i = 24;
                break;
            case WOOD:
                i = 28;
                break;
            case BRONZE:
                i = 32;
                break;
            case MARBLE:
                i = 36;
                break;
            case GRAPE:
                i = 40;
                break;
            case OLIVE:
                i = 44;
                break;
            case WOOL:
                i = 48;
                break;
            case ARMEMENT:
                i = 52;
                break;
            case SCULPTURE:
                i = 56;
                break;
            case OLIVE_OIL:
                i = 57;
                break;
            case WINE:
                i = 61;
            case NULL:
                i = 0;
                break;
        }
        if (res == Ressource.SCULPTURE)
            nbr = 1;
        return ImageLoader.getImage("Zeus_General", 7, i + nbr - 1);
    }

    /*
    public static void loadSet() {
        try {
            File imgFile = new File("Assets/Building/Stock/stock.png");
            imgSet = ImageIO.read(imgFile);
            listImage = new HashMap<>();

            int imagesCount = 66;
            BufferedImage images[] = new BufferedImage[imagesCount];
            int i = 0;
            int j = 0;
            int k = -1;
            int k2 = 0;
            for (int a = 0; a < images.length; a++) {
                if (i > 15) {
                    i = 0;
                    j++;
                }
                images[j] = imgSet.getSubimage(i * 58, j * 116, 58, 116);
                k2++;
                if (k2 > 3) {
                    k++;
                    k2 = 0;
                }
                if (k == 15 && k2 == 1)
                    k = 16;
                Ressource rc = null;
                switch (k) {
                    case -1:
                        rc = Ressource.SEA_URCHIN;
                        break;
                    case 0:
                        rc = Ressource.FISH;
                        break;
                    case 1:
                        rc = Ressource.MEAT;
                        break;
                    case 2:
                        rc = Ressource.CHEESE;
                        break;
                    case 3:
                        rc = Ressource.CARROT;
                        break;
                    case 4:
                        rc = Ressource.ONION;
                        break;
                    case 5:
                        rc = Ressource.CORN;
                        break;
                    case 6:
                        rc = Ressource.WOOD;
                        break;
                    case 7:
                        rc = Ressource.BRONZE;
                        break;
                    case 8:
                        rc = Ressource.MARBLE;
                        break;
                    case 9:
                        rc = Ressource.GRAPE;
                        break;
                    case 10:
                        rc = Ressource.OLIVE;
                        break;
                    case 11:
                        rc = Ressource.WOOL;
                        break;
                    case 12:
                        rc = Ressource.ARMEMENT;
                        break;
                    case 13:
                        rc = Ressource.OLIVE_OIL;
                        break;
                    case 14:
                        rc = Ressource.WINE;
                        break;
                    case 15:
                        rc = Ressource.SCULPTURE;
                        break;
                    case 16:
                        rc = Ressource.NULL;
                        break;
                    default:
                        break;

                }
                listImage.put(rc, images[j]);
                i++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    */
    @Override
    public void Render(Graphics g, int camX, int camY) {
        renderTile(cases.get(6), g, camX, camY);
        renderTile(cases.get(7), g, camX, camY);
        renderTile(cases.get(3), g, camX, camY);
        renderTile(cases.get(8), g, camX, camY);
        renderTile(cases.get(4), g, camX, camY);
        renderTile(cases.get(0), g, camX, camY);
        renderTile(cases.get(5), g, camX, camY);
        renderTile(cases.get(1), g, camX, camY);
        renderTile(cases.get(2), g, camX, camY);
    }

    public void renderTile(Case c, Graphics g, int camX, int camY) {
        com.exiro.Utils.Point p2 = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos(), (c.getyPos())), c.getWidth(), c.getHeight(), 1);
        g.drawImage(c.getImg(), camX + (int) p2.getX(), camY + (int) p2.getY(), null);
    }

    public void updateRender() {

        for (Case c2 : cases) {
            TileImage t = getRessourceTile(Ressource.CARROT, 4);
            c2.setImg(t.getImg());
            c2.setHeight(t.getH());
            c2.setWidth(t.getW());
        }
    }

    public Ressource getRan() {
        Random r = new Random();
        return Ressource.values()[r.nextInt(Ressource.values().length)];
    }

    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            for (Case c2 : cases) {
                TileImage t = getRessourceTile(getRan(), 4);
                c2.setImg(t.getImg());
                c2.setHeight(t.getH());
                c2.setWidth(t.getW());
                c2.setMainCase(false);
                c2.setSize(1);
            }
            cases.get(6).setImg(getImg());
            cases.get(6).setWidth(getWidth());
            cases.get(6).setHeight(getHeight());
            return true;
        }
        return false;
    }

    @Override
    public void process(double deltaTime) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    void addPopulation() {

    }



}
