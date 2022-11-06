package com.exiro.buildingList.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.fileManager.SoundLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.agriculture.Sheepherd;
import com.exiro.sprite.animals.Sheep;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class Sheepfold extends ResourceGenerator {



    double growth = 0.0;

    public Sheepfold(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, pop, 8, 30, 10, xPos, yPos, 2, 2, cases, built, city, 0, Resource.WOOL,5);

    }
    public Sheepfold() {
        super(false, ObjectType.SHEEPFOLD, BuildingCategory.FOOD, 0, 8, 30, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.WOOL,5);
        maxPerCarter = 1;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargements de " + getResource().getName(), 16, 20, 80);
        return bi;
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



    int sheepherdNbr = 0;

    public void createSheepherd() {
        Random r = new Random();
        if (city.getResourceManager().getSheeps().size() > 0) {
            Sheep destination = city.getResourceManager().getSheeps().get(r.nextInt(city.getResourceManager().getSheeps().size()));
            if (destination != null && destination.isAvailable() && AI.goTo(city, getAccess().get(0), city.getMap().getCase(destination.getXB(), destination.getYB()), FreeState.NON_BLOCKING.getI()) != null) {
                Sheepherd p = new Sheepherd(city, this, destination, (!destination.isMowed() && getStock() < getMaxStockOut()));
                destination.setAvailable(false);
                addSprite(p);
                sheepherdNbr++;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        sheepherdNbr = 0;
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
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            if (sheepherdNbr < 2) {
                createSheepherd();
            }
        }
    }

    public void sheepherdFinished(boolean createWool) {
        sheepherdNbr--;
        if(createWool)
            resourceCreated(1);
    }

    @Override
    protected void addPopulation() {

    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.SHEEP_FARM;
    }
}
