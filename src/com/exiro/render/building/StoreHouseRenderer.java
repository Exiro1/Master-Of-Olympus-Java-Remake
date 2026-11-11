package com.exiro.render.building;

import com.exiro.buildingList.stockage.StoreHouse;
import com.exiro.object.Case;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.List;

final class StoreHouseRenderer {

    /** Footprint cases drawn after case 6 and the working animation, back to front. */
    private static final int[] TILE_ORDER = {7, 3, 8, 4, 0, 5, 1, 2};

    private StoreHouseRenderer() {
    }

    /**
     * A storehouse is drawn tile by tile: each case shows the goods stored on it.
     */
    static void render(StoreHouse b, Graphics g, int camX, int camY) {
        List<Case> cases = b.getCases();
        drawTile(cases.get(6), g, camX, camY);
        if (b.isWorking())
            b.getBuildingSprites().get(0).Render(g, camX, camY);
        for (int i : TILE_ORDER)
            drawTile(cases.get(i), g, camX, camY);

        if (BuildingRenderers.DEBUG) {
            Case main = cases.get(6);
            Point p = IsometricRender.TwoDToIsoTexture(new Point(b.getxPos() - main.getZlvl(), b.getyPos() - main.getZlvl()), b.getWidth(), b.getHeight(), 1);
            g.drawString(b.getPop() + "/" + b.getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);
        }
    }

    private static void drawTile(Case c, Graphics g, int camX, int camY) {
        Point p = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos() - c.getZlvl(), c.getyPos() - c.getZlvl()), c.getWidth(), c.getHeight(), 1);
        g.drawImage(c.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
    }
}
