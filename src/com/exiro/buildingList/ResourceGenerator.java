package com.exiro.buildingList;

import com.exiro.moveRelated.FreeState;
import com.exiro.object.*;
import com.exiro.sprite.delivery.carter.*;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;

import java.util.ArrayList;

public abstract class ResourceGenerator extends Building {


    protected int maxPerCarter;
    int stock;
    int maxStockOut;
    Resource resource;
    boolean carterAvailable = true;
    boolean complex = false;
    Building returnFrom;
    boolean returnFromHome = false;


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


    @Override
    public void stop() {
        super.stop();
        carterAvailable = true;
        returnFromHome = false;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        manageCarter();
    }

    public void manageCarter() {

        if (stock > 0 && carterAvailable) {
            int toDeliver = Math.min(stock, maxPerCarter);
            stock -= toDeliver;
            addSprite(Carter.startDelivery(city,this,null,resource,toDeliver,0));
            carterAvailable = false;
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if(c instanceof Carter){
                if(((Carter) c).getCarterState() == Carter.CarterState.DONE){
                    toDestroy.add(c);
                    carterAvailable = true;
                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }

    }

    @Override
    public void populate(double deltaTime) {

    }

    public boolean isCarterAvailable() {
        return carterAvailable;
    }

    public void setCarterAvailable(boolean carterAvailable) {
        this.carterAvailable = carterAvailable;
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
