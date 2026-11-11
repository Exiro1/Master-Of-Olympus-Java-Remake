package com.exiro.render.building;

import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;

final class AgoraShopRenderer {

    private AgoraShopRenderer() {
    }

    /** Sprite 0 shows the goods on the stall (when stocked), sprite 1 the seller (when also working). */
    static void render(AgoraShopBuilding b, Graphics g, int camX, int camY) {
        Point p = IsometricRender.TwoDToIsoTexture(new Point(b.getxPos(), b.getyPos()), b.getWidth(), b.getHeight(), 2);
        g.drawImage(b.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        if (b.getReserve() > 0) {
            b.getBuildingSprites().get(0).Render(g, camX, camY);
            if (b.isWorking())
                b.getBuildingSprites().get(1).Render(g, camX, camY);
        }
    }
}
