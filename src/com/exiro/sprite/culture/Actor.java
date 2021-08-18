package com.exiro.sprite.culture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.DeliverySprite;

public class Actor extends DeliverySprite {


    public Actor(City c, ObjectClass destination, Case start, Case dest) {
        super("SprMain", 0, 104, 12, c, destination, start, 0);
        setOffsetY(-5);
        setTimeBetweenFrame(0.05f);
        setRoutePath(getAi().goTo(c, start, dest, FreeState.ALL_ROAD.getI()));
    }

    public Actor(City c, ObjectClass destination, Case start, int len) {
        super("SprMain", 0, 104, 12, c, destination, start, len);
        setOffsetY(-5);
        setTimeBetweenFrame(0.1f);
    }

    @Override
    public void deliverBuildings() {
        for (Building b : getBuildingsToDeliver()) {
            if (b instanceof House)
                ((House) b).setDrama(100);
        }
    }

}
