package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.Ressource;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StoreBuilding extends Building {

    HashMap<Ressource, Integer> stockage;
    int emptyCase = 8;

    HashMap<Ressource, Integer> reserved;
    int emptyCaseReserved = 8;

    public StoreBuilding(boolean isActive, BuildingType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
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

    public abstract void updateStock();

}
