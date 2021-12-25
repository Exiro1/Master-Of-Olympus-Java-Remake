package com.exiro.buildingList.culture;

import com.exiro.ai.AI;
import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.DeliverySprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.agriculture.Sheepherd;
import com.exiro.sprite.culture.Athlete;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class Gymnasium extends Building {


    double timeBeforeAthlete = 0;

    public Gymnasium(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public Gymnasium() {
        super(false, ObjectType.GYMNASIUM, BuildingCategory.CULTURE, 0, 7, 75, 2, 0, 0, 3, 3, null, false, GameManager.currentCity, 0);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 26, 32, getCity(), this);
            s.setOffsetX(67);
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
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Athlete) {
                if (ms.hasArrived && ms.getDestination() != this) {
                    ms.hasArrived = false;
                    ms.setRoutePath(AI.goTo(city,ms.getMainCase(),this.getAccess().get(0),FreeState.ALL_ROAD.getI()));
                    ms.setDestination(this);
                }else if(ms.hasArrived && ms.getDestination() == this){
                    toR.add(ms);
                }
            }
        }

        for (MovingSprite ms : toR) {
            removeSprites(ms);
        }

        timeBeforeAthlete += deltaTime;
        if (timeBeforeAthlete > 20) {
            timeBeforeAthlete = 0;
            createAthlete();
        }


    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    public void createAthlete() {
        Athlete p = new Athlete(city, null, this.getAccess().get(0), 40);
        addSprite(p);
    }

}
