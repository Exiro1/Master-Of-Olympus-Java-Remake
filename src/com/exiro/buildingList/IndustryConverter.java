package com.exiro.buildingList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.delivery.carter.Carter;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Time;

import java.util.ArrayList;

public abstract class IndustryConverter extends ResourceGenerator {


    int unitProduced, timeToTransform, unitNeeded;
    Resource needed;
    Time startConvertion;
    ConversionState state;
    protected int stockIn, incomming = 0, maxStock;

    public IndustryConverter(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resourceProduced, int timeToTransform, int unitProduced, int unitNeeded, Resource resourceNeeded, int maxStock) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resourceProduced, 1);
        this.timeToTransform = timeToTransform;
        this.unitProduced = unitProduced;
        this.unitNeeded = unitNeeded;
        this.needed = resourceNeeded;
        stockIn = 0;
        this.maxStock = maxStock;

    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        if(state == IndustryConverter.ConversionState.CONVERSION){
            bi.addText("Ce batiment contient " + stockIn + " chargements de " + resource.getName(), 16, 20, 80);
            bi.addText("La production est complétée à " + percentCompleted+"%", 16, 20, 110);
        }else {
            bi.addText("Ce batiment contient " + stockIn + " chargements de " + resource.getName(), 16, 20, 80);
            bi.addText("Il a besoin de  " + (unitNeeded - stockIn) + " chargements supplementaire pour commencer", 16, 20, 110);
        }
        return bi;
    }

    public BuildingSprite createBuildingSpriteWait() {
        BuildingSprite s = new BuildingSprite("SprAmbient", 0, 2436, 12, getCity(), this, Direction.SOUTH_EAST);
        s.setOffsetX(9);
        s.setOffsetY(16);
        s.setTimeBetweenFrame(0.1f);
        s.setComplex(true);
        return s;
    }

    public abstract void createBuildingSpriteWork();

    public abstract void createBSStock();

    public abstract void updateBSStock();


    public void initBSprite() {
        addSprite(createBuildingSpriteWait());
        createBuildingSpriteWork();
        createBSStock();
        getBuildingSprites().get(0).setVisible(true);
        getBuildingSprites().get(1).setVisible(false);
        getBuildingSprites().get(2).setVisible(false);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            initBSprite();
            return true;
        }
        return false;
    }


    public void setState(ConversionState state) {
        if (this.state == state)
            return;
        this.state = state;
        if (state == ConversionState.WAITING_RESOURCES) {
            getBuildingSprites().get(0).setVisible(true);
            getBuildingSprites().get(1).setVisible(false);
        } else {
            getBuildingSprites().get(0).setVisible(false);
            getBuildingSprites().get(1).setVisible(true);
        }
    }
    protected int lastStockIn = 0;




    public void updateStock(){
        if(stockIn>0) {
            if (lastStockIn != stockIn) {
                updateBSStock();
                getBuildingSprites().get(2).setVisible(true);
                lastStockIn = stockIn;
            }
        }else{
            getBuildingSprites().get(2).setVisible(false);
            if (lastStockIn != 0) {
                lastStockIn = 0;
            }
        }
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);
    }

    int percentCompleted= 0;

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        if (isWorking()) {

            if (stockIn + incomming < maxStock && carterAvailable) {
                refuel();
            }

            if (state == ConversionState.CONVERSION) {
                int timeToComplete = (int) (timeToTransform * (((float) getPopMax() / (float) getPop())));
                int timeSinceStart = GameManager.getInstance().getTimeManager().daysSince(startConvertion);
                percentCompleted = Math.min((int)(((float)timeSinceStart/(float)timeToComplete)*100),100);
                if (timeSinceStart > timeToComplete && carterAvailable) {
                    resourceCreated(unitProduced);
                    setState(ConversionState.WAITING_RESOURCES);
                }
            }
            if (state == ConversionState.WAITING_RESOURCES) {
                if (stockIn >= unitNeeded) {
                    setState(ConversionState.CONVERSION);
                    startConvertion = GameManager.getInstance().getTimeManager().getTime();
                    stockIn -= unitNeeded;

                }
            }


            updateStock();
        }
    }

    public void addIncomming(int incomming){
        this.incomming+=incomming;
    }

    public int roomForInputResources(){
        return  Math.max(0,maxStock - (stockIn + incomming));
    }

    public void refuel() {
        ArrayList<Building> stores = new ArrayList<>(city.getBuildingList(ObjectType.GRANARY));
        stores.addAll(city.getBuildingList(ObjectType.STOCK));
        for (Building b : stores) {
            if(!(b instanceof StoreBuilding sb))
                continue;
            if (sb.hasStockAvailable(needed)) {
                int command = Math.min(maxStock - stockIn - incomming, sb.getStockAvailable(needed));
                incomming += command;
                sb.reserveUnstockage(needed,command);
                addSprite(Carter.startCommand(city,this,sb,needed,command));
                carterAvailable = false;
            }
        }
    }

    public void delivered(int amount){
        incomming -= amount;
        stockIn += amount;
    }

    @Override
    public void manageCarter() {
        super.manageCarter();
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
    public void stop() {
        super.stop();
        incomming = 0;
    }

    @Override
    protected void addPopulation() {

    }

    public enum ConversionState {WAITING_RESOURCES, CONVERSION}
}
