package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;
import com.exiro.Sprite.BuildingSprite;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Granary extends Building {

    // le vrai
    HashMap<Ressource, Integer> stockage;
    int emptyCase = 8;
    // en attente
    HashMap<Ressource, Integer> reserved;
    int emptyCaseReserved = 8;

    public Granary(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, pop, 15, 50, 10, xPos, yPos, 4, 4, cases, built, city, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
    }

    public Granary() {
        super(false, BuildingType.GRANARY, BuildingCategory.MARKET, 0, 15, 50, 10, 0, 0, 4, 4, null, false, GameManager.currentCity, 0);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
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
     * stock ressources in the stockage
     *
     * @param amount
     * @param r
     * @return success
     */
    public boolean stock(int amount, Ressource r) {
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
        //updateStock();
        return true;
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

}
