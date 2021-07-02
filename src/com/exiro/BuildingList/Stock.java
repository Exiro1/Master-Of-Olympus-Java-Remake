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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Stock extends Building {


    /*
    sprite chariot : 8428 SprMain


     */

    // le vrai
    HashMap<Ressource, Integer> stockage;
    int emptyCase = 8;
    // en attente
    HashMap<Ressource, Integer> reserved;
    int emptyCaseReserved = 8;



    public Stock(boolean isActive, BuildingType type, String path, int size, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID, Map<Ressource, Integer> stock) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
    }


    public Stock(int xPos, int yPos, City c) {
        //super(false, BuildingType.STOCK, "Assets/Building/Stock/stockInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        super(false, BuildingType.STOCK, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
    }

    public Stock() {
        //super(false, BuildingType.STOCK, "Assets/Building/Stock/stockInactive.png", 58, 114, 1, BuildingCategory.STOCKAGE, 0, 2, 150, 30, xPos, yPos, 3, 3, new ArrayList<>(), false, c, 0);
        super(false, BuildingType.STOCK, BuildingCategory.STOCKAGE, 0, 2, 150, 30, 0, 0, 3, 3, new ArrayList<>(), false, GameManager.currentCity, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
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
                break;
            case NULL:
                i = 114;
                break;
        }
        if (res == Ressource.SCULPTURE || res == Ressource.NULL)
            nbr = 1;
        return ImageLoader.getImage("Zeus_General", 7, i + nbr - 1);
    }


    /**
     * get free space for a ressource type
     *
     * @param r Ressource
     * @return free space
     */
    public int getFreeSpace(Ressource r) {
        int space = 0;
        reserved.putIfAbsent(r, 0);
        int used = reserved.get(r);
        space = (emptyCaseReserved * 4) / r.getWeight() + (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight(); //surement simplifiable mais j ai un qi d'huitre aujourd'hui
        return space;
    }

    /**
     * reserve space for ressources
     *
     * @param r      ressource
     * @param amount amount of ressource to stock
     * @return amount of ressource that cannot be stored
     */
    public int reserve(Ressource r, int amount) {
        reserved.putIfAbsent(r, 0);
        int used = reserved.get(r);
        int spaceLeftInCase = (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight();
        if (spaceLeftInCase > 0) {
            reserved.replace(r, reserved.get(r) + Math.min(amount, spaceLeftInCase));
            amount -= Math.min(amount, spaceLeftInCase);
        }
        if (amount > 0) {
            int reservePossible = emptyCaseReserved >= (amount * r.getWeight()) / 4.0 ? (int) Math.ceil((amount * r.getWeight()) / 4.0) : emptyCaseReserved;
            reserved.replace(r, reserved.get(r) + (amount * r.getWeight() <= emptyCaseReserved * 4 ? amount : emptyCaseReserved * (4 / r.getWeight())));
            amount -= (amount * r.getWeight() <= emptyCaseReserved * 4 ? amount : emptyCaseReserved * (4 / r.getWeight()));
            emptyCaseReserved -= reservePossible;
        }
        return amount;
    }

    /**
     * Cancel a space reservation
     *
     * @param r      Ressource
     * @param amount amount
     */
    public void unReserved(Ressource r, int amount) {
        reserved.putIfAbsent(r, 0);
        int before = reserved.get(r);
        int after = reserved.get(r) - amount;
        int stockNeededBefore = ((int) Math.ceil((before * r.getWeight() / 4.0)));
        int stockNeededAfter = ((int) Math.ceil((after * r.getWeight() / 4.0)));
        emptyCaseReserved += stockNeededBefore - stockNeededAfter;
        if (reserved.get(r) >= amount) {
            reserved.replace(r, reserved.get(r) - amount);
        } else {
            reserved.replace(r, 0); //no supposed to happen
        }
    }

    /**
     * reserve + store
     *
     * @param amount    amount to store
     * @param ressource ressource to store
     * @return amount of ressource that cannot be stored
     */
    public int instantStock(int amount, Ressource ressource) {
        int avail = reserve(ressource, amount);
        stock(ressource, avail);
        return amount - avail;
    }

    /**
     * stock ressources in the stockage
     *
     * @param amount
     * @param r
     * @return success
     */
    public boolean stock(Ressource r, int amount) {
        //check that the amount specified has been reserved
        stockage.putIfAbsent(r, 0);
        if (reserved.get(r) >= stockage.get(r) + amount) {
            int temp = amount;
            int used = stockage.get(r);
            int spaceLeftInCase = (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight();
            if (spaceLeftInCase > 0) {
                temp -= Math.min(temp, spaceLeftInCase);
            }
            if (temp > 0) {
                int reservePossible = emptyCaseReserved >= (temp * r.getWeight()) / 4.0 ? (int) Math.ceil((temp * r.getWeight()) / 4.0) : emptyCase;
                emptyCase -= reservePossible;
            }
            stockage.replace(r, stockage.get(r) + amount);
        }
        updateStock();
        return true;
    }

    /**
     * get ressources from the stock
     *
     * @param amount amount of ressources to retrieve
     * @param r      Ressource
     * @return amount of ressources retrieved
     */
    public int unStock(Ressource r, int amount) {
        if (!stockage.containsKey(r))
            return 0;

        int ret = 0;
        int before = stockage.get(r);
        int after = stockage.get(r) - amount;
        int stockNeededBefore = ((int) Math.ceil((before * r.getWeight() / 4.0)));
        int stockNeededAfter = ((int) Math.ceil((after * r.getWeight() / 4.0)));

        int beforer = reserved.get(r);
        int afterr = reserved.get(r) - amount;
        int stockNeededBeforer = ((int) Math.ceil((beforer * r.getWeight() / 4.0)));
        int stockNeededAfterr = ((int) Math.ceil((afterr * r.getWeight() / 4.0)));

        emptyCase += stockNeededBefore - stockNeededAfter;
        emptyCaseReserved += stockNeededBeforer - stockNeededAfterr;

        if (stockage.get(r) >= amount) {
            stockage.replace(r, stockage.get(r) - amount);
            reserved.replace(r, reserved.get(r) - amount);
            ret = amount;
        } else {
            ret = stockage.get(r);
            stockage.replace(r, 0); //no supposed to happen
            reserved.replace(r, 0);
        }
        return ret;
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

        com.exiro.Utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), 1);
        g.drawString(getPop() + "/" + getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);

    }

    public void renderTile(Case c, Graphics g, int camX, int camY) {
        com.exiro.Utils.Point p2 = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos(), (c.getyPos())), c.getWidth(), c.getHeight(), 1);
        g.drawImage(c.getImg(), camX + (int) p2.getX(), camY + (int) p2.getY(), null);
    }

    public Ressource getRan() {
        Random r = new Random();
        return Ressource.values()[r.nextInt(Ressource.values().length)];
    }

    public void updateStock() {
        int i = 0;
        for (Ressource r : stockage.keySet()) {
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
            setCaseRes(1, Ressource.NULL, i);
        }
    }

    public void setCaseRes(int amount, Ressource r, int index) {
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
                TileImage t = getRessourceTile(Ressource.NULL, 1);
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
    void addPopulation() {

    }


}
