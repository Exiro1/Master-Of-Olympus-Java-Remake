package com.exiro.sprite.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.agriculture.SmallHolding;
import com.exiro.constructionList.SmallHoldingFruit.OliveTree;
import com.exiro.constructionList.SmallHoldingFruit.SmallHoldingTree;
import com.exiro.constructionList.SmallHoldingFruit.Vine;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;

import java.util.ArrayList;

public class Grower extends AgricultureSprite {


    //5504 12 walk
    //5608 10 grape treating
    //5688 10 olive treating
    //5768 10 grape gather
    //5848 10 olive gather


    boolean gather, process, harvester;
    boolean grape; //grape or olive
    GrowerState gstate;
    SmallHolding origin;
    SmallHoldingTree tree;
    ArrayList<SmallHoldingTree> trees;
    int index = 0;

    public Grower(City c, SmallHolding origin, ArrayList<SmallHoldingTree> destinations, boolean gather, boolean harvester) {
        super("SprMain", 0, 5504, 12, c, destinations.get(0));
        this.gather = gather;
        this.process = !gather;
        this.grape = destinations.get(0) instanceof Vine;
        this.origin = origin;
        Case start = origin.getAccess().get(0);
        setX(start.getxPos());
        setY(start.getyPos());
        setXB(start.getxPos());
        setYB(start.getyPos());
        setRoutePath(AI.goTo(c, start, c.getMap().getCase(this.destination.getXB(), this.destination.getYB()), FreeState.NON_BLOCKING.getI(), false));
        this.createRessource = gather;
        tree = destinations.get(0);
        tree.setAvailable(false);
        this.harvester = harvester;
        this.trees = destinations;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);


        if (hasArrived) {
            if (getDestination() == origin && !finished) {
                origin.growerFinished(createRessource, !(tree instanceof OliveTree), harvester);
                finished = true;
            }
            if (gather && gstate != GrowerState.GATHERING) {
                if (tree.getXB() == getXB() && tree.getYB() == getYB()) {
                    fullAnimCounter = 0;
                    index = 0;
                    gather();
                } else {
                    hasArrived = false;
                    setRoutePath(AI.goTo(c, c.getMap().getCase(getXB(), getYB()), c.getMap().getCase(tree.getXB(), tree.getYB()), FreeState.NON_BLOCKING.getI(), false));
                }
            }
            if (process && gstate != GrowerState.PROCESSING) {
                processTree();
            }
            if (gstate == GrowerState.GATHERING && fullAnimCounter > 2) {
                gather = false;
                tree.resourceGathered();
                gstate = GrowerState.WALKING;
                hasArrived = false;
                unidir = false;
                tree.setAvailable(true);
                setLocalID(5504);
                setFrameNumber(12);
                setRoutePath(AI.goTo(c, getMainCase(), origin.getAccess().get(0), FreeState.NON_BLOCKING.getI(), false));
                setDestination(origin);
            } else if (gstate == GrowerState.PROCESSING && fullAnimCounter > 6) {
                gstate = GrowerState.WALKING;
                hasArrived = false;
                unidir = false;
                tree.setAvailable(true);
                setLocalID(5504);
                setFrameNumber(12);

                if (index < trees.size() - 1) {
                    index++;
                    tree = trees.get(index);
                    this.grape = tree instanceof Vine;
                    tree.setAvailable(false);
                    setRoutePath(AI.goTo(c, getMainCase(), c.getMap().getCase(tree.getXB(), tree.getYB()), FreeState.NON_BLOCKING.getI(), false));
                    setDestination(tree);
                } else {
                    process = false;
                    setRoutePath(AI.goTo(c, getMainCase(), origin.getAccess().get(0), FreeState.NON_BLOCKING.getI(), false));
                    setDestination(origin);
                }
            }

        }


    }

    public void gather() {
        gstate = GrowerState.GATHERING;
        fullAnimCounter = 0;
        //setDir(Direction.NORD_EST);
        setLocalID(grape ? 5768 : 5848);
        setFrameNumber(10);
        //unidir = true;
    }

    public void processTree() {
        gstate = GrowerState.PROCESSING;
        //unidir = true;
        fullAnimCounter = 0;
        //setDir(Direction.NORD_EST);
        setLocalID(grape ? 5608 : 5688);
        setFrameNumber(10);
    }

    enum GrowerState {
        WALKING, GATHERING, PROCESSING
    }


}
