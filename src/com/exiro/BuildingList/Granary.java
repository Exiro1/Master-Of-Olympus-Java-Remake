package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;
import com.exiro.Render.IsometricRender;
import com.exiro.Sprite.BuildingSprite;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;
import com.exiro.Utils.Point;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Granary extends Building {

    // le vrai
    HashMap<Ressource, Integer> stockage;
    int emptyCase = 8;
    // en attente
    HashMap<Ressource, Integer> reserved;
    int emptyCaseReserved = 8;

    ArrayList<TileImage> stockedImg;

    public Granary(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, pop, 15, 50, 10, xPos, yPos, 4, 4, cases, built, city, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
        stockedImg = new ArrayList<>();
    }

    public Granary() {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, 0, 15, 50, 10, 0, 0, 4, 4, null, false, GameManager.currentCity, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
        stockedImg = new ArrayList<>();
    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite("Zeus_General", 3, 29, 16, city, this);
            s.setTimeBetweenFrame(0.1f);
            s.setOffsetX(111);
            s.setOffsetY(31);
            addSprite(s);
            updateStock();
        }
        return succ;
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
     * put ressources in the stockage
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
        com.exiro.Utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        g.drawString(getPop() + "/" + getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);

        //render only buildingSprite because movingSprite are render separately

        int i = 0;
        for (BuildingSprite s : bsprites) {
            if ((isActive() && getPop() > 0) || i > 0)
                s.Render(g, camX, camY);
            i++;
        }

    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {
        if (isActive() && getPop() > 0) {

        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    void addPopulation() {

    }

    public void updateStock() {
        int i = 0;

        ArrayList<BuildingSprite> toR = new ArrayList<>();
        for (int l = 1; l < bsprites.size(); l++) {
            toR.add(bsprites.get(l));
        }
        for (BuildingSprite bs : toR) {
            removeSprites(bs);
        }
        toR.clear();
        for (Ressource r : stockage.keySet()) {
            int k = 0;
            for (int j = 0; j < stockage.get(r); j++) {
                k++;
                if (k * r.getWeight() >= 4) {
                    setCaseRes(r, i);
                    k = 0;
                    i++;
                }
            }
            if (k > 0) {
                setCaseRes(r, i);
                i++;
            }
        }
        for (; i < 8; i++) {
            setCaseRes(Ressource.NULL, i);
        }
    }

    public int getRessourceIndex(Ressource res) {
        int i = 0;
        switch (res) {

            case SEA_URCHIN:
                i = 45;
                break;
            case FISH:
                i = 46;
                break;
            case MEAT:
                i = 47;
                break;
            case CHEESE:
                i = 48;
                break;
            case CARROT:
                i = 49;
                break;
            case ONION:
                i = 50;
                break;
            case CORN:
                i = 51;
                break;
            case NULL:
                return 0;
        }
        return i;
    }

    public void setCaseRes(Ressource r, int index) {
        int i = 0;
        if ((i = getRessourceIndex(r)) == 0)
            return;
        int ofx = 0, ofy = 0;
        switch (index) {
            case 0:
                ofx = 33;
                ofy = -48;
                break;
            case 1:
                ofx = 53;
                ofy = -55;
                break;
            case 2:
                ofx = 74;
                ofy = -62;
                break;
            case 3:
                ofx = 94;
                ofy = -69;
                break;
            case 4:
                ofx = 114;
                ofy = -76;
                break;
            case 5:
                ofx = 38;
                ofy = -65;
                break;
            case 6:
                ofx = 58;
                ofy = -72;
                break;
            case 7:
                ofx = 78;
                ofy = -79;
                break;
        }

        BuildingSprite bs = new BuildingSprite("Zeus_General", 3, i, 1, city, this);
        bs.setOffsetX(ofx);
        bs.setOffsetY(ofy);
        addSprite(bs);
    }


}
