package com.exiro.buildingList.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.agriculture.Goatherd;
import com.exiro.sprite.animals.Goat;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class Dairy extends ResourceGenerator {

    double growth = 0;
    int speedFactor = 1;


    public Dairy() {
        super(false, ObjectType.DAIRY, BuildingCategory.FOOD, 0, 8, 30, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.CHEESE,3);
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
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 69, 21, getCity(), this);
            s.setOffsetX(20);
            s.setOffsetY(-50);
            s.setTimeBetweenFrame(0.1f);
            s.setComplex(true);
            addSprite(s);
            return true;
        }
        return false;
    }

    int goatherdNbr = 0;

    @Override
    public void stop() {
        super.stop();
        goatherdNbr = 0;
    }

    public void createGoatherd() {
        Random r = new Random();
        if (city.getGoats().size() > 0) {
            Goat destination = city.getGoats().get(r.nextInt(city.getGoats().size()));
            if (destination != null && destination.isAvailable()  && AI.goTo(city, getAccess().get(0), city.getMap().getCase(destination.getXB(), destination.getYB()), FreeState.NON_BLOCKING.getI()) != null) {
                Goatherd p = new Goatherd(city, this, destination, (!destination.isMilked() && getStock() < getMaxStockOut()));
                destination.setAvailable(false);
                addSprite(p);
                goatherdNbr++;
            }
        }
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Goatherd) {
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
            if (goatherdNbr < 2) {
                createGoatherd();
            }
        }
    }

    public void goatherdFinished(boolean createCheese) {
        goatherdNbr--;
        if(createCheese)
            resourceCreated(1);
    }

    @Override
    protected void addPopulation() {

    }
}
