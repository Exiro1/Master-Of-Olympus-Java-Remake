package com.exiro.buildingList.stockage;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.IsometricRender;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Stock extends StoreBuilding {


    public Stock(boolean isActive, ObjectType type, String path, int size, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID, Map<Resource, Integer> stock) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }


    public Stock(int xPos, int yPos, City c) {
        //super(false, BuildingType.STOCK, "Assets/Building/Stock/stockInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        super(false, ObjectType.STOCK, BuildingCategory.STOCKAGE, 0, 12, 40, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
    }

    public Stock() {
        //super(false, BuildingType.STOCK, "Assets/Building/Stock/stockInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        super(false, ObjectType.STOCK, BuildingCategory.STOCKAGE, 0, 12, 40, 30, 0, 0, 3, 3, new ArrayList<>(), false, GameManager.currentCity, 0);
    }

    public TileImage getRessourceTile(Resource res, int nbr) {
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
                break;
            case NULL:
                i = 114;
                break;
        }
        if (res == Resource.SCULPTURE || res == Resource.NULL)
            nbr = 1;
        return ImageLoader.getImage("Zeus_General", 7, i + nbr - 1);
    }


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

        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - cases.get(6).getZlvl(), (getyPos() - cases.get(6).getZlvl())), getWidth(), getHeight(), 1);
        g.drawString(getPop() + "/" + getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);

    }

    public void renderTile(Case c, Graphics g, int camX, int camY) {
        com.exiro.utils.Point p2 = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos() - c.getZlvl(), (c.getyPos() - c.getZlvl())), c.getWidth(), c.getHeight(), 1);
        g.drawImage(c.getImg(), camX + (int) p2.getX(), camY + (int) p2.getY(), null);
    }

    public Resource getRan() {
        Random r = new Random();
        return Resource.values()[r.nextInt(Resource.values().length)];
    }

    @Override
    public boolean canStock(Resource r) {
        return r.canStock();
    }

    public void updateStock() {
        int i = 0;
        for (Resource r : stockage.keySet()) {
            int k = 0;
            for (int j = 0; j < stockage.get(r); j++) {
                k++;
                if (k * r.getWeight() >= 4) {
                    setCaseRes(k, r, i);
                    k = 0;
                    i++;
                }
            }
            if (k > 0) {
                setCaseRes(k, r, i);
                i++;
            }
        }
        for (; i < 8; i++) {
            setCaseRes(1, Resource.NULL, i);
        }
    }

    public void setCaseRes(int amount, Resource r, int index) {
        Case c = null;
        if (index < 6)
            c = cases.get(index);
        if (index >= 6)
            c = cases.get(index + 1);
        TileImage t = getRessourceTile(r, amount);
        c.setImg(t.getImg());
        c.setHeight(t.getH());
        c.setWidth(t.getW());
    }

    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            for (Case c2 : cases) {
                TileImage t = getRessourceTile(Resource.NULL, 1);
                c2.setImg(t.getImg());
                c2.setHeight(t.getH());
                c2.setWidth(t.getW());
                c2.setMainCase(false);
                c2.setSize(1);
            }
            cases.get(6).setImg(getImg());
            cases.get(6).setWidth(getWidth());
            cases.get(6).setHeight(getHeight());

            updateStock();
            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {

    }

    @Override
    public void process(double deltaTime) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }


}
