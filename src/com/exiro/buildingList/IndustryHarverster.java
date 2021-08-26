package com.exiro.buildingList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.Harvester;
import com.exiro.sprite.MovingSprite;

import java.util.ArrayList;

public class IndustryHarverster extends ResourceGenerator {


    protected int timeToHarvest; //in day
    protected int harvesterNbr; //how many people go harvesting
    protected int unitPerHarvester; //unit of raw resource harvested per people
    protected int unitNeeded; //unit number needed to create 1 resource

    protected int unit;


    public IndustryHarverster(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource, int timeToHarvest, int harvesterNbr, int unitPerHarvester, int unitNeeded) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
        this.timeToHarvest = timeToHarvest;
        this.harvesterNbr = harvesterNbr;
        this.unitPerHarvester = unitPerHarvester;
        this.unitNeeded = unitNeeded;
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Harvester) {
                if (((Harvester) ms).finished()) {
                    harvestFinished((Harvester) ms);
                    toR.add(ms);
                }
            }
        }
        for (MovingSprite m : toR) {
            removeSprites(m);
        }
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);

    }


    public void harvestFinished(Harvester harvester) {
        unit += unitPerHarvester;
        if (unit >= unitNeeded) {
            resourceCreated(1);
            unit -= unitNeeded;
        }
    }


    @Override
    protected void addPopulation() {

    }

}
