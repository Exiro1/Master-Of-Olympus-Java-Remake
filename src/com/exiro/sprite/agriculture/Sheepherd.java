package com.exiro.sprite.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.agriculture.Sheepfold;
import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.sprite.animals.Sheep;

import java.util.Map;

public class Sheepherd extends AgricultureSprite {

    boolean cut, cutting = false;
    boolean wait, waiting = false;
    Sheepfold sheepfold;
    Sheep sheep;


    public Sheepherd(City c, Sheepfold sheepfold, Sheep sheep, boolean cut) {
        super("SprMain", 0, 3414, 12, c, sheep);
        this.sheepfold = sheepfold;
        this.sheep = sheep;
        Case start = sheepfold.getAccess().get(0);
        setX(start.getxPos());
        setY(start.getyPos());
        setXB(start.getxPos());
        setYB(start.getyPos());
        setRoutePath(AI.goTo(c, start, c.getMap().getCase(sheep.getXB(), sheep.getYB()), FreeState.NON_BLOCKING.getI(), false));
        this.cut = cut;
        this.createRessource = cut;
        this.wait = !cut;
        if (cut) {
            sheep.stop();
        }
    }

    public void cut() {
        cutting = true;
        sheep.cut();
        fullAnimCounter = 0;
        setDir(Direction.NORTH_EAST);
        setLocalID(3518);
        setFrameNumber(12);
        unidir = true;
    }

    public void waiting() {
        waiting = true;
        unidir = true;
        fullAnimCounter = 0;
        setDir(Direction.NORTH_EAST);
        setLocalID(3626);
        setFrameNumber(10);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);


        if (hasArrived) {
            if (getDestination() == sheepfold && !finished) {
                sheepfold.sheepherdFinished(createRessource);
                finished = true;
            }
            if (cut && !cutting) {
                if (sheep.getXB() == getXB() && sheep.getYB() == getYB()) {
                    fullAnimCounter = 0;
                    index = 0;
                    cut();
                } else {
                    hasArrived = false;
                    setRoutePath(AI.goTo(c, c.getMap().getCase(getXB(), getYB()), c.getMap().getCase(sheep.getXB(), sheep.getYB()), FreeState.NON_BLOCKING.getI(), false));
                }
            }
            if (wait && !waiting) {
                waiting();
            }
            if (cutting && fullAnimCounter > 1) {
                cutting = false;
                cut = false;
                hasArrived = false;
                unidir = false;
                sheep.start();
                setLocalID(3530);
                setFrameNumber(12);
                setRoutePath(AI.goTo(c, getMainCase(), sheepfold.getAccess().get(0), FreeState.NON_BLOCKING.getI(), false));
                setDestination(sheepfold);
            } else if (waiting && fullAnimCounter > 6) {
                waiting = false;
                wait = false;
                hasArrived = false;
                unidir = false;
                sheep.addDays(-2);
                setLocalID(3414);
                setFrameNumber(12);
                setRoutePath(AI.goTo(c, getMainCase(), sheepfold.getAccess().get(0), FreeState.NON_BLOCKING.getI(), false));
                setDestination(sheepfold);
            }

        }


    }

    @Override
    public void delete() {
        super.delete();

        sheep.setAvailable(true);
        if (sheep.isStop() || sheep.isCut())
            sheep.start();

    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
