package com.exiro.sprite.culture;

import com.exiro.ai.AI;
import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.DeliverySprite;

public class Philosopher extends DeliverySprite {

    public Philosopher(City c, ObjectClass destination, Case start, Case dest) {
        super("SprMain", 0, 12127, 12, c, destination, start, 0);
        setOffsetY(-10);
        setTimeBetweenFrame(0.05f);
        setRoutePath(AI.goTo(c, start, dest, FreeState.ALL_ROAD.getI()));
    }

    public Philosopher(City c, ObjectClass destination, Case start, int len) {
        super("SprMain", 0, 12127, 12, c, destination, start, len);
        setTimeBetweenFrame(0.1f);
        setOffsetY(-10);
    }

    @Override
    public void deliverBuildings() {
        for (Building b : getBuildingsToDeliver()) {
            if (b instanceof House)
                ((House) b).setPhilosophia(100);
        }
    }
}
