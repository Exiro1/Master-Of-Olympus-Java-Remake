package com.exiro.buildingList.culture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.agriculture.Sheepherd;
import com.exiro.sprite.culture.Actor;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class TheaterSchool extends Building {


    double timBeforeActor = 0;

    public TheaterSchool(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public TheaterSchool() {
        super(false, ObjectType.THEATERSCHOOL, BuildingCategory.CULTURE, 0, 10, 35, 10, 0, 0, 3, 3, null, false, GameManager.currentCity, 0);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 59, 24, getCity(), this);
            s.setOffsetX(66);
            s.setOffsetY(9);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            if (isWorking())
                s.process(delta);
        }
    }


    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        if (isWorking()) {
            timBeforeActor += deltaTime;
            if (timBeforeActor > 20) {
                timBeforeActor = 0;
                createActor();
            }
        }

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Actor) {
                if (ms.hasArrived) {
                    toR.add(ms);
                }
            }
        }
        for (MovingSprite ms : toR) {
            removeSprites(ms);
        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }


    public void createActor() {
        Random r = new Random();
        if(city.getTheaters().size() == 0)
            return;
        Theater destination = city.getTheaters().get(r.nextInt(city.getTheaters().size()));
        if (destination != null && destination.isWorking()) {
            Actor a = new Actor(city, destination, this.getAccess().get(0), destination.getAccess().get(0));
            addSprite(a);
        }
    }


}
