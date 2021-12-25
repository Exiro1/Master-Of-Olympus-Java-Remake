package com.exiro.sprite.delivery;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.sprite.DeliverySprite;
import com.exiro.systemCore.GameManager;

public class AgoraDelivery extends DeliverySprite {

    Agora agora;

    public AgoraDelivery(City c, ObjectClass destination, Case start, int len, Agora agora) {
        super("SprMain", 0, 0, 12, c, destination, start, len);
        setTimeBetweenFrame(0.05);
        setOffsetY(-5);
        this.agora = agora;
    }
    public AgoraDelivery() {
        super("SprMain", 0, 3, 12, GameManager.currentCity, null, null, 0);
        setTimeBetweenFrame(0.05);
        setOffsetY(-5);
        setType(ObjectType.SHEEP);
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
                        int f = Math.min(agoraShopBuilding.getReserve(), Math.min(((House) b).getMaxWool()- (int) ((House) b).getWool(), 4));
                        ((House) b).addWool(f);
                        agoraShopBuilding.refuel(-f);
                    } else if (agoraShopBuilding.getShop() == AgoraShop.OIL) {
                        int f = Math.min(agoraShopBuilding.getReserve(), Math.min(((House) b).getMaxHDO()- (int) ((House) b).getHdo(), 4));
                        ((House) b).addHdo(f);
                        agoraShopBuilding.refuel(-f);
                    }
                }

            }
        }
    }


}
