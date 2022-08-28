package com.exiro.sprite;

import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.reader.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.Resource;

import java.util.ArrayList;
import java.util.Map;

public class AgoraSupplierChief extends MovingSprite {

    ArrayList<Resource> needed;
    //equivalent nourriture récupéré
    int resourcesGathered;
    AgoraShopBuilding building;

    public AgoraSupplierChief(City c, Case origin, ObjectClass destination, ArrayList<Resource> needed, AgoraShopBuilding building) {
        super("SprMain", 0, 2886, 12, c, destination);
        this.needed = needed;
        setX(origin.getxPos());
        setY(origin.getyPos());
        setXB(Math.round(getX()));
        setYB(Math.round(getY()));

        path = c.getPathManager().getPathTo(origin, destination.getAccess().get(0), FreeState.ALL_ROAD.i);
        offsetX = 0;
        offsetY = -8;
        this.building = building;
    }


    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }

    public ArrayList<Resource> getNeeded() {
        return needed;
    }

    public int getResourcesGathered() {
        return resourcesGathered;
    }

    public void setResourcesGathered(int resourcesGathered) {
        this.resourcesGathered = resourcesGathered;
    }

    public AgoraShopBuilding getAType() {
        return building;
    }

}
