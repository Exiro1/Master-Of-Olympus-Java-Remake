package com.exiro.buildingList.culture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.culture.Philosopher;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class College extends Building {

    double timBeforePhilo = 0;

    public College(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public College() {
        super(false, ObjectType.COLLEGE, BuildingCategory.CULTURE, 0, 12, 65, 2, 0, 0, 3, 3, null, false, GameManager.currentCity, 0);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 1, 24, getCity(), this);
            s.setOffsetX(65);
            s.setOffsetY(15);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        if (isWorking()) {
            timBeforePhilo += deltaTime;
            if (timBeforePhilo > 20) {
                timBeforePhilo = 0;
                createPhilosopher();
            }
        }

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Philosopher) {
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
    public void processSprite(double delta) {
        for (Sprite s : getSprites()) {
            if (isActive() && getPop() > 0)
                s.process(delta, 0);
        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    public void createPhilosopher() {
        Random r = new Random();
        if(city.getBuildingList(ObjectType.PODIUM).size() == 0)
            return;
        Podium destination = (Podium) city.getBuildingList(ObjectType.PODIUM).get(r.nextInt(city.getBuildingList(ObjectType.PODIUM).size()));
        if (destination != null && destination.isWorking()) {
            Philosopher p = new Philosopher(city, destination, this.getAccess().get(0), destination.getAccess().get(0));
            addSprite(p);
        }
    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.PHILOSOPHY;
    }
}
