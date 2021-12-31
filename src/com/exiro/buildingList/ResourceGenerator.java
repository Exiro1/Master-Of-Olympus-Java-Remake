package com.exiro.buildingList;

import com.exiro.moveRelated.FreeState;
import com.exiro.object.*;
import com.exiro.sprite.Carter;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.complexCarter.ComplexCarter;
import com.exiro.sprite.complexCarter.Trolley;
import com.exiro.sprite.complexCarter.TrolleyDriver;

import java.util.ArrayList;

public abstract class ResourceGenerator extends Building {


    protected int maxPerCarter;
    int stock;
    int maxStockOut;
    Resource resource;


    public ResourceGenerator(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource,int maxStockOut ) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID);
        this.resource = resource;
        this.maxPerCarter = resource.getMaxPerCart();
        this.maxStockOut = maxStockOut ;
    }



    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            if (isWorking())
                s.process(delta);
        }
    }

    public void resourceCreated(int unit) {
        stock += unit;
        stock = Math.min(stock, maxStockOut);
    }

    public void addCarter(Resource r, int amount) {
        if (r == Resource.WOOD || r == Resource.MARBLE || r == Resource.SCULPTURE) {
            ComplexCarter cc = new ComplexCarter(city, null, this.getAccess().get(0), resource, amount);
            addSprite(cc.getDriver());
            addSprite(cc.getPuller());
            addSprite(cc.getTrolley());
            complex = true;
        } else {
            Carter carter = new Carter(city, null, this, resource, amount);
            addSprite(carter);
            complex = false;
        }
        carterAvailable = false;
    }
    public void returnHome(Building from, boolean complex){
        if (complex) {
            ComplexCarter cc = new ComplexCarter(city, this, from.getAccess().get(0), Resource.NULL, 0);
            cc.setRoutePath(city.getPathManager().getPathTo(from.getAccess().get(0).getxPos(), from.getAccess().get(0).getyPos(), getAccess().get(0).getxPos(), getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i));
            addSprite(cc.getDriver());
            addSprite(cc.getPuller());
            addSprite(cc.getTrolley());
        } else {
            Carter carter = new Carter(city, this, from, Resource.NULL, 0);
            carter.setPath(city.getPathManager().getPathTo(carter.getXB(), carter.getYB(), getAccess().get(0).getxPos(), getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i));
            addSprite(carter);
        }
    }

    @Override
    public void stop() {
        super.stop();
        carterAvailable = true;
        returnFromHome = false;
        refueling = false;
    }

    boolean carterAvailable = true;
    boolean complex = false;
    Building returnFrom;
    boolean returnFromHome = false;
    protected boolean refueling = false; //a revoir
    public void manageCarter() {
        if(refueling)
            return;
        if (stock > 0 && carterAvailable) {
            int toDeliver = Math.min(stock, maxPerCarter);
            stock -= toDeliver;
            addCarter(resource, toDeliver);
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {

            if (c.hasArrived && c instanceof Carter) {
                Carter carter = (Carter) c;
                ObjectClass des = c.getDestination();
                if (des == null || !des.isActive() || ((Building) des).isDeleted()) { //object supprimée / inactif -> on relance la recherche
                    c.hasArrived = false;
                    c.setRoutePath(null);
                    c.setDestination(null);
                } else if (des instanceof StoreBuilding) {
                    StoreBuilding g = (StoreBuilding) des;
                    g.stock(resource, carter.getCurrentDelivery());
                    carter.setAmount(carter.getAmount() - carter.getCurrentDelivery());
                    if (carter.getAmount() > 0) {
                        c.hasArrived = false;
                        c.setRoutePath(null);
                    } else {
                        toDestroy.add(c);
                        returnFrom = g;
                        returnFromHome = true;
                    }
                }else if(des == this){
                    toDestroy.add(c);
                    carterAvailable = true;
                }else if(des instanceof IndustryConverter){
                    ((IndustryConverter) des).delivered(carter.getCurrentDelivery());
                    carter.setAmount(carter.getAmount() - carter.getCurrentDelivery());
                    if (carter.getAmount() > 0) {
                        c.hasArrived = false;
                        c.setRoutePath(null);
                    }else {
                        toDestroy.add(c);
                        returnFrom = (Building) c.getDestination();
                        returnFromHome = true;
                    }
                }
            } else if (c.hasArrived && c instanceof TrolleyDriver) {
                ComplexCarter cc = ((TrolleyDriver) c).getCc();
                ObjectClass des = cc.getDest();
                if (des == null || !des.isActive() || ((Building) des).isDeleted()) { //object supprimée / inactif -> on relance la recherche
                    cc.setArrived(false);
                    cc.setRoutePath(null);
                    cc.setDest(null);
                } else if (des instanceof StoreBuilding) {
                    StoreBuilding g = (StoreBuilding) des;
                    g.stock(resource, cc.getTrolley().getCurrentDelivery());
                    cc.getTrolley().setAmount(cc.getTrolley().getAmount() - cc.getTrolley().getCurrentDelivery());
                    if (cc.getTrolley().getAmount() > 0) {
                        cc.setArrived(false);
                        cc.setRoutePath(null);
                    } else {
                        toDestroy.add(cc.getDriver());
                        toDestroy.add(cc.getPuller());
                        toDestroy.add(cc.getTrolley());
                        returnFrom = g;
                        returnFromHome = true;
                    }
                }else if(des == this){
                    toDestroy.add(cc.getDriver());
                    toDestroy.add(cc.getPuller());
                    toDestroy.add(cc.getTrolley());
                    carterAvailable = true;
                }


            }

        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
        if(returnFromHome){
            returnFromHome = false;
            returnHome(returnFrom,complex);
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
            } else if (ms instanceof Trolley) {
                Trolley c = (Trolley) ms;
                ObjectClass obj = c.getDestination();
                if (obj instanceof StoreBuilding) {
                    StoreBuilding g = (StoreBuilding) obj;
                    g.unReserved(resource, c.getCurrentDelivery());
                }
            }
        }
    }

    public int getMaxPerCarter() {
        return maxPerCarter;
    }

    public int getMaxStockOut() {
        return maxStockOut;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
