package com.exiro.buildingList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Direction;
import com.exiro.sprite.Harvester;
import com.exiro.sprite.MovingSprite;

import java.util.ArrayList;

public class IndustryHarverster extends ResourceGenerator {


    protected int timeToHarvest; //in day
    protected int harvesterNbr; //how many people go harvesting
    protected int harvester = 0; //actual harvester
    protected int unitPerHarvester; //unit of raw resource harvested per people
    protected int unitNeeded; //unit number needed to create 1 resource

    protected int unit;
    IndustryConverter.ConversionState state;

    public IndustryHarverster(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource, int timeToHarvest, int harvesterNbr, int unitPerHarvester, int unitNeeded) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource,3);
        this.timeToHarvest = timeToHarvest;
        this.harvesterNbr = harvesterNbr;
        this.unitPerHarvester = unitPerHarvester;
        this.unitNeeded = unitNeeded;

    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Ce batiment contient " + unit + " chargements de " + resource.getName(), 16, 20, 80);
        return bi;
    }

    public BuildingSprite createBuildingSpriteWait() {
        BuildingSprite s = new BuildingSprite("SprAmbient", 0, 2436, 12, getCity(), this, Direction.SOUTH_EAST);
        s.setOffsetX(9);
        s.setOffsetY(16);
        s.setTimeBetweenFrame(0.1f);
        s.setComplex(true);
        if(getBuildingSprites().size()==0)
            addSprite(s);
        setSprite(0,s);
        return s;
    }

    public void createBuildingSpriteWork() {

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
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

    }

    @Override
    public void stop() {
        super.stop();
        harvester = 0;
    }

    public void setState(IndustryConverter.ConversionState state) {
        if (this.state == state)
            return;
        this.state = state;
        if (state == IndustryConverter.ConversionState.WAITING_RESOURCES) {
            createBuildingSpriteWait();
        } else {
            createBuildingSpriteWork();
        }
    }

    public void harvestFinished(Harvester harvester) {
        unit += unitPerHarvester;
        if (unit >= unitNeeded) {
            resourceCreated(1);
            unit -= unitNeeded;
        }
        setUnitBSprite();
    }

    public void setUnitBSprite() {

    }

    public IndustryConverter.ConversionState getState() {
        return state;
    }

    @Override
    protected void addPopulation() {

    }

}
