package com.exiro.sprite.culture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.DeliverySprite;

public class Athlete extends DeliverySprite {

    public Athlete(City c, ObjectClass destination, Case start, Case dest) {
        super("SprMain", 0, 10587, 12, c, destination, start, 0);
        setRoutePath(getAi().goTo(c, start, dest, FreeState.ACTIVE_ROAD.getI()));
        setOffsetY(-5);
        setTimeBetweenFrame(0.05f);
    }

    public Athlete(City c, ObjectClass destination, Case start, int len) {
        super("SprMain", 0, 10587, 12, c, destination, start, len);
        setOffsetY(-5);
        setTimeBetweenFrame(0.05f);
    }

    @Override
    public void deliverBuildings() {
        for (Building b : getBuildingsToDeliver()) {
            if (b instanceof House)
                ((House) b).setGymnasium(100);
        }
    }
}
