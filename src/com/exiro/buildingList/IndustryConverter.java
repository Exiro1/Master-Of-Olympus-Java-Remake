package com.exiro.buildingList;

import com.exiro.ai.AI;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.Carter;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Time;

import java.util.ArrayList;

public class IndustryConverter extends ResourceGenerator {


    int unitProduced, timeToTransform, unitNeeded;
    Resource needed;
    Time startConvertion;
    ConversionState state;
    int stockIn, incomming = 0, maxStock;

    public IndustryConverter(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resourceProduced, int timeToTransform, int unitProduced, int unitNeeded, Resource resourceNeeded, int maxStock) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resourceProduced);
        this.timeToTransform = timeToTransform;
        this.unitProduced = unitProduced;
        this.unitNeeded = unitNeeded;
        this.needed = resourceNeeded;
        stockIn = 0;
        this.maxStock = maxStock;
        state = ConversionState.WAITING_RESOURCES;
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);

        if (isWorking()) {
            if (state == ConversionState.CONVERSION) {
                if (GameManager.getInstance().getTimeManager().daysSince(startConvertion) > (timeToTransform * (((float) getPopMax() / (float) getPop())))) {
                    resourceCreated(unitProduced);
                    state = ConversionState.WAITING_RESOURCES;
                }
            }
            if (state == ConversionState.WAITING_RESOURCES) {
                if (stockIn >= unitNeeded) {
                    state = ConversionState.CONVERSION;
                    startConvertion = GameManager.getInstance().getTimeManager().getTime();
                    stockIn -= unitNeeded;
                }
            }

            if (stockIn + incomming < maxStock) {
                refuel();
            }

        }
    }

    public void refuel() {
        for (StoreBuilding sb : city.getStorage()) {
            if (sb.hasStockAvailable(needed)) {
                int retrieved = sb.unStock(needed, Math.min(maxStock - stockIn - incomming, sb.getStockAvailable(needed)));
                incomming += retrieved;
                Carter carter = new Carter(city, this, sb, needed, retrieved);
                carter.setRoutePath(AI.goTo(city, sb.getAccess().get(0), this.getAccess().get(0), FreeState.ALL_ROAD.getI()));
                addSprite(carter);
            }
        }
    }

    @Override
    public void manageCarter() {
        super.manageCarter();
        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if (c.hasArrived && c instanceof Carter) {
                if (c.getDestination() == this) {
                    incomming -= ((Carter) c).getAmmount();
                    stockIn += ((Carter) c).getAmmount();
                    toDestroy.add(c);
                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
    }

    @Override
    protected void addPopulation() {

    }

    enum ConversionState {WAITING_RESOURCES, CONVERSION}
}
