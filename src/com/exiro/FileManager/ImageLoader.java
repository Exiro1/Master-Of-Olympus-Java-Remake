package com.exiro.FileManager;

import com.exiro.BuildingList.House;
import com.exiro.BuildingList.Stock;
import com.exiro.Sprite.Immigrant;

public class ImageLoader {

    /**
     * Charge les image pur eviter de les chargé pour chauqe objet , --> diminue de ouf la ram utilisé du genre 2.5go a 200mo
     */
    public static void loadImage() {
        House.loadSet();
        Stock.loadSet();
        Immigrant.loadSprite();
    }


}
