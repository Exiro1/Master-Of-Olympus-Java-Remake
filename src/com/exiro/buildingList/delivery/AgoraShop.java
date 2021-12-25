package com.exiro.buildingList.delivery;

public enum AgoraShop {
    FOOD(0, 600), WOOL(2, 600), OIL(4, 600), WINE(6, 600), WEAPON(8, 8), HORSE(10, 4), EMPTY(15, 0);

    final int id, querry;

    AgoraShop(int id, int maxQuerry) {
        this.id = id;
        this.querry = maxQuerry;
    }

    public int getId() {
        return id;
    }

    public int getQuerry() {
        return querry;
    }
}
