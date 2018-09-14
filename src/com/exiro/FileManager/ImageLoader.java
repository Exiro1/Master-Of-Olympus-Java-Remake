package com.exiro.FileManager;

import com.exiro.BuildingList.House;
import com.exiro.BuildingList.Stock;
import com.exiro.Sprite.Immigrant;

public class ImageLoader {

    public static void loadImage() {
        House.loadSet();
        Stock.loadSet();
        Immigrant.loadSprite();
    }


}
