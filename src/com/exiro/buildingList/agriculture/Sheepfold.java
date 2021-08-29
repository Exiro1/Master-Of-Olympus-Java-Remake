package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.agriculture.Sheepherd;
import com.exiro.sprite.animals.Sheep;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class Sheepfold extends ResourceGenerator {


    public Sheepfold(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
    }

    double growth = 0.0;

    public Sheepfold(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, pop, 8, 30, 10, xPos, yPos, 2, 2, cases, built, city, 0, Resource.WOOL);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 56, 12, getCity(), this);
            s.setOffsetX(24);
            s.setOffsetY(2);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    public Sheepfold() {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, 0, 8, 30, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.WOOL);
    }

    int sheepherdNbr = 0;

    public void createSheepherd() {
        Random r = new Random();
        if (city.getSheeps().size() > 0) {
            Sheep destination = city.getSheeps().get(r.nextInt(city.getSheeps().size()));
            if (destination != null && destination.isAvailable()) {
                Sheepherd p = new Sheepherd(city, this, destination, !destination.isMowed());
                destination.setAvailable(false);
                addSprite(p);
                sheepherdNbr++;
            }
        }
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Sheepherd) {
                if (ms.hasArrived && ms.getDestination() == this) {
                    toR.add(ms);
                }
            }
        }
        for (MovingSprite ms : toR) {
            removeSprites(ms);
        }

    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive() && getPop() > 0) {
            if (sheepherdNbr < 2) {
                createSheepherd();
            }
        }
    }

    public void sheepherdFinished() {
        sheepherdNbr--;
        resourceCreated(1);
    }

    @Override
    protected void addPopulation() {

    }
}
