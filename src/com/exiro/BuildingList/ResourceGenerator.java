package com.exiro.BuildingList;

import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Object.Ressource;
import com.exiro.Sprite.Carter;
import com.exiro.Sprite.MovingSprite;
import com.exiro.Sprite.Sprite;

import java.util.ArrayList;

public abstract class ResourceGenerator extends Building {


    final int maxPerCarter;
    int stock;
    Ressource resource;


    public ResourceGenerator(boolean isActive, BuildingType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Ressource ressource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID);
        this.resource = ressource;
        this.maxPerCarter = ressource.getMaxPerCart();
    }


    public void resourceCreated(int unit) {
        stock += unit;
    }

    public void manageCarter() {
        if (stock > 0) {
            int toDeliver = Math.min(stock, maxPerCarter);
            stock -= toDeliver;
            Carter carter = new Carter(city, null, this, resource, toDeliver);
            addSprite(carter);
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if (c.hasArrived) {
                Carter carter = (Carter) c;
                ObjectClass des = c.getDestination();
                if (des == null || !des.isActive() || ((Building) des).isDeleted()) { //object supprimée / inactif -> on relance la recherche
                    c.hasArrived = false;
                    c.setRoutePath(null);
                    c.setDestination(null);
                } else if (des instanceof StoreBuilding) {
                    StoreBuilding g = (StoreBuilding) des;
                    g.stock(resource, carter.getCurrentDelivery());
                    carter.setAmmount(carter.getAmmount() - carter.getCurrentDelivery());
                    if (carter.getAmmount() > 0) {
                        c.hasArrived = false;
                        c.setRoutePath(null);
                    } else {
                        toDestroy.add(c);
                    }
                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
    }

    @Override
    public void populate(double deltaTime) {
        manageCarter();
    }


    @Override
    public void delete() {
        super.delete();
        for (MovingSprite ms : msprites) {
            if (ms instanceof Carter) {
                Carter c = (Carter) ms;
                ObjectClass obj = c.getDestination();
                if (obj instanceof StoreBuilding) {
                    StoreBuilding g = (StoreBuilding) obj;
                    g.unReserved(resource, c.getCurrentDelivery());
                }
            }
        }
    }

}
