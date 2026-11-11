package com.exiro.render.building;

import com.exiro.buildingList.House;
import com.exiro.utils.Point;

import java.awt.*;

final class HouseRenderer {

    private HouseRenderer() {
    }

    static void render(House h, Graphics g, int camX, int camY) {
        Point p = BuildingRenderers.drawBase(h, g, camX, camY);
        if (BuildingRenderers.DEBUG)
            g.drawString((int) h.getFood() + " " + (int) h.getWater(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 45);
        if (h.isWorking())
            BuildingRenderers.drawSprites(h, g, camX, camY);
    }
}
