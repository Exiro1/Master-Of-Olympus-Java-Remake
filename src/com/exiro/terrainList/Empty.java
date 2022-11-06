package com.exiro.terrainList;

import com.exiro.fileManager.SoundLoader;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

public class Empty extends Terrain {

    int number;

    public Empty(int number, City c) {
        super(ObjectType.EMPTY, true, 0, 0, c, true, true, false, 1);
        this.number = number;
        this.setLocalID(number);
        updateImg();
    }

    public Empty() {
        super(ObjectType.EMPTY, true, 0, 0, GameManager.currentCity, true, true, false, 1);
        this.number = 105;
        this.setLocalID(number);
        updateImg();
    }

    @Override
    public void process(double deltaTime, int deltaDays) {

    }

    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.FARMLAND;
    }
}
