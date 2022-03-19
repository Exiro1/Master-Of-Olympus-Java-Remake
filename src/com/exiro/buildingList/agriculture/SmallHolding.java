package com.exiro.buildingList.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.constructionList.SmallHoldingFruit.SmallHoldingTree;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.agriculture.Grower;
import com.exiro.sprite.delivery.carter.Carter;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SmallHolding extends ResourceGenerator {


    private int growerNbr, stockOlive, stockGrape, harvesterNbr;
    private ArrayList<SmallHoldingTree> trees = new ArrayList<>();
    private ArrayList<SmallHoldingTree> matureTrees = new ArrayList<>();

    public SmallHolding() {
        super(false, ObjectType.SMALLHOLDING, BuildingCategory.FOOD, 0, 12, 40, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.OLIVE, 3);
        maxPerCarter = 1;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + stockOlive + " chargement d'" + Resource.OLIVE.getName(), 16, 20, 80);
        bi.addText("Reserve de " + stockGrape + " chargements de " + Resource.GRAPE.getName(), 16, 20, 100);
        return bi;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 91, 10, getCity(), this);
            s.setOffsetX(38);
            s.setOffsetY(1);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            city.getResourceManager().addSmallHolding(this);
            return true;
        }
        return false;
    }

    @Override
    public void delete() {
        super.delete();
        city.getResourceManager().removeSmallHolding(this);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {
            if (growerNbr < 1) {
                createGrower();
            }
            if (GameManager.getInstance().getTimeManager().getMonth() >= 0 && GameManager.getInstance().getTimeManager().getMonth() < 2) {
                startHarvesting();
            } else if (GameManager.getInstance().getTimeManager().getMonth() >= 9 && GameManager.getInstance().getTimeManager().getMonth() < 11) {
                startHarvesting();
            }
        }
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);

        ArrayList<MovingSprite> toR = new ArrayList<>();
        for (MovingSprite ms : getMovingSprites()) {
            if (ms instanceof Grower) {
                if (ms.hasArrived && ms.getDestination() == this) {
                    toR.add(ms);
                }
            }
        }
        for (MovingSprite ms : toR) {
            removeSprites(ms);
        }
    }


    public void startHarvesting() {
        Random r = new Random();
        for (int i = harvesterNbr; i < 3; i++) {
            if (getMatureTrees().size() > 0) {
                SmallHoldingTree destination = getMatureTrees().get(r.nextInt(getMatureTrees().size()));
                if (!destination.isMature())
                    continue;
                if (destination != null && destination.isAvailable() && AI.goTo(city, getAccess().get(0), city.getMap().getCase(destination.getXB(), destination.getYB()), FreeState.NON_BLOCKING.getI(), false) != null) {
                    Grower g = new Grower(city, this, new ArrayList<>(Collections.singletonList(destination)), true, true);
                    destination.setAvailable(false);
                    addSprite(g);
                    harvesterNbr++;
                }
            }
        }
    }


    public void createGrower() {
        Random r = new Random();
        if (trees.size() > 0) {
            ArrayList<SmallHoldingTree> dests = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                SmallHoldingTree destination = trees.get(r.nextInt(trees.size()));
                if (destination != null && destination.isAvailable() && AI.goTo(city, getAccess().get(0), city.getMap().getCase(destination.getXB(), destination.getYB()), FreeState.NON_BLOCKING.getI(), false) != null) {
                    dests.add(destination);
                }
            }
            if (dests.size() > 0) {
                Grower g = new Grower(city, this, dests, false, false);
                addSprite(g);
                growerNbr++;
            }
        }
    }

    @Override
    public void manageCarter() {

        if (stockOlive > 0 && carterAvailable) {
            int toDeliver = Math.min(stockOlive, maxPerCarter);
            stockOlive -= toDeliver;
            addSprite(Carter.startDelivery(city, this, null, Resource.OLIVE, toDeliver, 0));
            carterAvailable = false;
        } else if (stockGrape > 0 && carterAvailable) {
            int toDeliver = Math.min(stockGrape, maxPerCarter);
            stockGrape -= toDeliver;
            addSprite(Carter.startDelivery(city, this, null, Resource.GRAPE, toDeliver, 0));
            carterAvailable = false;
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if (c instanceof Carter) {
                if (((Carter) c).getCarterState() == Carter.CarterState.DONE) {
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
    protected void addPopulation() {

    }

    public void resourceCreatedSpecial(int unit, boolean grapes) {
        if (grapes) {
            stockGrape += unit;
            stockGrape = Math.min(stockGrape, 5);
        } else {
            stockOlive += unit;
            stockOlive = Math.min(stockOlive, 5);
        }
    }

    public void growerFinished(boolean createRessource, boolean grapes, boolean harvester) {
        if (harvester) {
            harvesterNbr--;
        } else {
            growerNbr--;
        }
        if (createRessource)
            resourceCreatedSpecial(1, grapes);
    }


    public void addTree(SmallHoldingTree t) {
        trees.add(t);
    }

    public List<SmallHoldingTree> getMatureTrees() {
        return trees.stream().filter(SmallHoldingTree::isMature).collect(Collectors.toList());
    }

    public void removeTree(SmallHoldingTree t) {
        trees.remove(t);
    }
}
