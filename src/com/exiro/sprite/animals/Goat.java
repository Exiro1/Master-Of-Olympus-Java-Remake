package com.exiro.sprite.animals;

import com.exiro.ai.AnimalsAI;
import com.exiro.fileManager.SoundLoader;
import com.exiro.reader.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Time;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class Goat extends Animal {

    //11227 normal non tondu 12
    //11331 normal eating 16
    //11459 par terre non tondu 8

    //7872 normal tondu
    //7976 eating tondu
    //8040 par terre tondu

    AnimalsAI ai;
    GoatState state;
    boolean available = true;
    int days = 180;
    Time start;
    double timeUntilChange = 10;
    boolean milked = true;
    double checkTime = 0;
    GoatState lastState;

    public Goat() {
        super("SprMain", 0, 11227, 12, GameManager.currentCity, null);
        setType(ObjectType.GOAT);
        ai = new AnimalsAI();
        setState(GoatState.NORMAL_EATING);
        start = GameManager.getInstance().getTimeManager().getTime();
    }

    public Goat(City c) {
        super("SprMain", 0, 11227, 12, c, null);
        setType(ObjectType.GOAT);
        ai = new AnimalsAI();
        setState(GoatState.NORMAL_EATING);
        start = GameManager.getInstance().getTimeManager().getTime();
    }

    public void setState(GoatState state) {
        this.state = state;
        switch (state) {

            case NORMAL:
                setLocalID(11227);
                setFrameNumber(12);
                break;
            case NORMAL_EATING:
                setLocalID(11395);
                setFrameNumber(8);
                break;
            case NORMAL_SLEEPING:
                setLocalID(11459);
                setFrameNumber(8);
                break;
        }
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        if (state == GoatState.BEING_MILKED || state == GoatState.STOP)
            return;

        checkTime += deltaTime;
        super.process(deltaTime, deltaDays);

        if (milked && checkTime > 3) {
            checkTime = 0;
            if (GameManager.getInstance().getTimeManager().daysSince(start) > days) {
                milked = false;
            }
        }

        if (timeUntilChange > 0)
            timeUntilChange -= deltaTime;

        if (hasArrived && (state == GoatState.NORMAL)) {
            Random r = new Random();
            timeUntilChange = r.nextInt(30) + 15;
            setState(GoatState.NORMAL_EATING);
        }


        if (timeUntilChange <= 0 && state != GoatState.NORMAL) {
            Random r = new Random();
            setRoutePath(ai.roaming(c, 2 + r.nextInt(4), FreeState.MEADOW.getI(), getMainCase()));
            hasArrived = false;
            setState(GoatState.NORMAL);
        }
    }

    @Override
    public void delete() {
        super.delete();
        c.getResourceManager().removeGoat(this);
    }

    public void stop() {
        lastState = state;
        setState(GoatState.STOP);
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
        return state == GoatState.STOP;
    }

    public boolean isBeingMilked() {
        return state == GoatState.BEING_MILKED;
    }

    public void milk() {
        setState(GoatState.BEING_MILKED);
        milked = true;
        days = 360;
        start = GameManager.getInstance().getTimeManager().getTime();
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        if (state != GoatState.BEING_MILKED)
            super.Render(g, camX, camY);

    }

    //@Override


    public boolean isMilked() {
        return milked;
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    enum GoatState {
        NORMAL, NORMAL_EATING, NORMAL_SLEEPING, STOP, BEING_MILKED
    }

    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.GOAT;
    }
}
