package com.exiro.buildingList.delivery;

public enum AgoraShop {
    FOOD(0), WOOL(2), OIL(4), WINE(6), WEAPON(8), HORSE(10), EMPTY(15);

    final int id;

    AgoraShop(int id) {
        this.id = id;
    }

}
