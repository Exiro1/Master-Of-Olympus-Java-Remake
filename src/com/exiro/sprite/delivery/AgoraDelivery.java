package com.exiro.sprite.delivery;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.sprite.DeliverySprite;

public class AgoraDelivery extends DeliverySprite {

    Agora agora;

    public AgoraDelivery(City c, ObjectClass destination, Case start, int len, Agora agora) {
        super("SprMain", 0, 0, 12, c, destination, start, len);
        setTimeBetweenFrame(0.05);
        setOffsetY(-5);
        this.agora = agora;
    }

    @Override
    public void deliverBuildings() {
        for (Building b : getBuildingsToDeliver()) {
            if (b instanceof House) {
                for (AgoraShopBuilding agoraShopBuilding : agora.getShopsBuildings()) {
                    if (agoraShopBuilding.getShop() == AgoraShop.FOOD) {
                        int f = Math.min(agoraShopBuilding.getReserve(), Math.min(((House) b).getMaxfood() - (int) ((House) b).getFood(), 50));
                        ((House) b).addFood(f);
                        agoraShopBuilding.refuel(-f);
                    } else if (agoraShopBuilding.getShop() == AgoraShop.WOOL) {
                        int f = Math.min(agoraShopBuilding.getReserve(), Math.min(((House) b).getMaxWool(), 5));
                        ((House) b).addWool(f);
                        agoraShopBuilding.refuel(-f);
                    } else if (agoraShopBuilding.getShop() == AgoraShop.OIL) {
                        int f = Math.min(agoraShopBuilding.getReserve(), Math.min(((House) b).getMaxWool(), 5));
                        ((House) b).addHdo(f);
                        agoraShopBuilding.refuel(-f);
                    }
                }

            }
        }
    }


}
