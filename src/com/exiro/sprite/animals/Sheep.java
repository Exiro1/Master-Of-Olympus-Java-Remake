package com.exiro.sprite.animals;

import com.exiro.ai.AnimalsAI;
import com.exiro.reader.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Meadow;
import com.exiro.utils.Time;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Sheep extends Animal {

    //3182 normal non tondu
    //3286 normal eating
    //3350 par terre non tondu

    //7872 normal tondu
    //7976 eating tondu
    //8040 par terre tondu

    AnimalsAI ai;
    SheepState state;
    boolean available = true;
    int days = 180;
    Time start;
    double timeUntilChange = 10;
    boolean mowed = true;
    double checkTime = 0;
    SheepState lastState;

    public Sheep() {
        super("SprMain", 0, 3182, 12, GameManager.currentCity, null);
        setType(ObjectType.SHEEP);
        ai = new AnimalsAI();
        setState(SheepState.MOWED_EATING);
        start = GameManager.getInstance().getTimeManager().getTime();
    }

    public Sheep(City c) {
        super("SprMain", 0, 3182, 12, c, null);
        setType(ObjectType.SHEEP);
        ai = new AnimalsAI();
        setState(SheepState.MOWED_EATING);
        start = GameManager.getInstance().getTimeManager().getTime();
    }


    public void setState(SheepState state) {
        this.state = state;
        switch (state) {

            case NORMAL:
                setLocalID(3182);
                setFrameNumber(12);
                break;
            case NORMAL_SLEEPING:
                setLocalID(3350);
                setFrameNumber(8);
                break;
            case NORMAL_EATING:
                setLocalID(3286);
                setFrameNumber(8);
                break;
            case MOWED:
                setLocalID(7872);
                setFrameNumber(12);
                break;
            case MOWED_EATING:
                setLocalID(7976);
                setFrameNumber(8);
                break;
            case MOWED_SLEEPING:
                setLocalID(8040);
                setFrameNumber(8);
                break;
        }
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        if (state == SheepState.BEING_CUT || state == SheepState.STOP)
            return;
        checkTime += deltaTime;
        super.process(deltaTime, deltaDays);
        if (mowed && checkTime > 3) {
            checkTime = 0;
            if (GameManager.getInstance().getTimeManager().daysSince(start) > days) {
                mowed = false;
                setState(SheepState.values()[state.ordinal() - 3]);
            }
        }

        if (timeUntilChange > 0)
            timeUntilChange -= deltaTime;

        if (hasArrived && (state == SheepState.MOWED || state == SheepState.NORMAL)) {
            Random r = new Random();
            timeUntilChange = r.nextInt(30) + 15;
            if (mowed) {
                setState(SheepState.MOWED_EATING);
            } else {
                setState(SheepState.NORMAL_EATING);
            }

        }


        if (timeUntilChange <= 0 && state != SheepState.MOWED && state != SheepState.NORMAL) {
            Random r = new Random();
            setRoutePath(ai.roaming(c, 2 + r.nextInt(4), FreeState.MEADOW.getI(), getMainCase()));
            hasArrived = false;

            if (mowed) {
                setState(SheepState.MOWED);
            } else {
                setState(SheepState.NORMAL);
            }

        }
    }

    @Override
    public void delete() {
        super.delete();
        c.getResourceManager().removeSheep(this);
    }

    public void stop() {
        lastState = state;
        setState(SheepState.STOP);
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void addDays(int days) {
        this.days += days;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean b) {
        available = b;
    }

    public void start() {
        setState(lastState);
    }

    public boolean isStop() {
        return state == SheepState.STOP;
    }

    public boolean isCut() {
        return state == SheepState.BEING_CUT;
    }

    public void cut() {
        setState(SheepState.BEING_CUT);
        mowed = true;
        days = 360;
        start = GameManager.getInstance().getTimeManager().getTime();
        lastState = SheepState.values()[lastState.ordinal() + 3];
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        if (state != SheepState.BEING_CUT)
            super.Render(g, camX, camY);

    }

    //@Override
    public boolean build(int xPos, int yPos) {

        if (c.getMap().getCase(xPos, yPos).getTerrain() instanceof Meadow && c.getMap().getCase(xPos, yPos).getObject() == null) {
            setX(xPos);
            setY(yPos);
            setXB(xPos);
            setYB(yPos);
            c.getResourceManager().addSheep(this);
            setMainCase(c.getMap().getCase(getXB(), getYB()));
            return true;
        }

        return true;
    }

    //@Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        if (c.getMap().getCase(xPos, yPos) == null)
            return null;
        if (c.getMap().getCase(xPos, yPos).getTerrain() instanceof Meadow && c.getMap().getCase(xPos, yPos).getObject() == null) {
            ArrayList<Case> cc = new ArrayList<>();
            cc.add(c.getMap().getCase(xPos, yPos));
            return cc;
        }
        return null;
    }

    public boolean isMowed() {
        return mowed;
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    enum SheepState {
        NORMAL, NORMAL_EATING, NORMAL_SLEEPING, MOWED, MOWED_EATING, MOWED_SLEEPING, STOP, BEING_CUT;
    }
}
