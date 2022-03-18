package com.exiro.sprite.agriculture;

import com.exiro.ai.AI;
import com.exiro.buildingList.agriculture.Dairy;
import com.exiro.depacking.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.sprite.animals.Goat;

import java.util.Map;

public class Goatherd extends AgricultureSprite {

    boolean milk, milking = false;
    boolean wait, waiting = false;
    Dairy dairy;
    Goat goat;

    //2376

    public Goatherd(City c, Dairy dairy, Goat goat, boolean cut) {
        super("SprMain", 0, 2376, 12, c, goat);
        this.dairy = dairy;
        this.goat = goat;
        Case start = dairy.getAccess().get(0);
        setX(start.getxPos());
        setY(start.getyPos());
        setXB(start.getxPos());
        setYB(start.getyPos());
        setRoutePath(AI.goTo(c, start, c.getMap().getCase(this.goat.getXB(), this.goat.getYB()), FreeState.NON_BLOCKING.getI()));
        this.milk = cut;
        this.createRessource = cut;
        this.wait = !cut;
        if (cut) {
            this.goat.stop();
        }
    }

    public void milk() {
        milking = true;
        goat.milk();
        fullAnimCounter = 0;
        setDir(Direction.NORTH_EAST);
        setLocalID(2480);
        setFrameNumber(8);
        unidir = true;
    }

    public void waiting() {
        waiting = true;
        unidir = true;
        fullAnimCounter = 0;
        setDir(Direction.NORTH_EAST);
        setLocalID(2584);
        setFrameNumber(10);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);


        if (hasArrived) {
            if (getDestination() == dairy && !finished) {
                dairy.goatherdFinished(createRessource);
                finished = true;
            }
            if (milk && !milking) {
                if (goat.getXB() == getXB() && goat.getYB() == getYB()) {
                    fullAnimCounter = 0;
                    index = 0;
                    milk();
                } else {
                    hasArrived = false;
                    setRoutePath(AI.goTo(c, c.getMap().getCase(getXB(), getYB()), c.getMap().getCase(goat.getXB(), goat.getYB()), FreeState.NON_BLOCKING.getI()));
                }
            }
            if (wait && !waiting) {
                waiting();
            }
            if (milking && fullAnimCounter > 1) {
                milking = false;
                milk = false;
                hasArrived = false;
                unidir = false;
                goat.start();
                setLocalID(2488);
                setFrameNumber(12);
                setRoutePath(AI.goTo(c, getMainCase(), dairy.getAccess().get(0), FreeState.NON_BLOCKING.getI()));
                setDestination(dairy);
            } else if (waiting && fullAnimCounter > 6) {
                waiting = false;
                wait = false;
                hasArrived = false;
                unidir = false;
                goat.addDays(-2);
                setLocalID(2376);
                setFrameNumber(12);
                setRoutePath(AI.goTo(c, getMainCase(), dairy.getAccess().get(0), FreeState.NON_BLOCKING.getI()));
                setDestination(dairy);
            }

        }


    }

    @Override
    public void delete() {
        super.delete();

        goat.setAvailable(true);
        if (goat.isStop() || goat.isBeingMilked())
            goat.start();

    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }
}
