package com.exiro.render.building;

import com.exiro.buildingList.agriculture.Farm;
import com.exiro.reader.TileImage;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;

final class FarmRenderer {

    /** Field cases (dx, dy from the farm's position) showing the crop's growth. */
    private static final int[][] FIELD = {{0, 0}, {1, 0}, {2, -2}, {2, -1}, {2, 0}};

    private FarmRenderer() {
    }

    static void render(Farm f, Graphics g, int camX, int camY) {
        BuildingRenderers.DEFAULT.render(f, g, camX, camY);
        TileImage growth = f.getGrowthImg();
        if (growth == null)
            return;
        for (int[] d : FIELD) {
            Point p = IsometricRender.TwoDToIsoTexture(new Point(f.getxPos() + d[0], f.getyPos() + d[1]), growth.getW(), growth.getH(), 1);
            g.drawImage(growth.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        }
    }
}
