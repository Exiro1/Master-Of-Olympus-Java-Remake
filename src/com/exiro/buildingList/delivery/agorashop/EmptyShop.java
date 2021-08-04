package com.exiro.buildingList.delivery.agorashop;

import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.object.ObjectType;

public class EmptyShop extends AgoraShopBuilding {


    public EmptyShop() {
        super(AgoraShop.EMPTY, ObjectType.AGORAEMPTY);
        this.setPopMax(0);
    }
}
