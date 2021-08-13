package com.exiro.render;


import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.render.layout.GameLayout;
import com.exiro.render.layout.GameWindow;
import com.exiro.terrainList.Elevation;
import com.exiro.utils.Point;

public class IsometricRender {


    private static final int TILE_WIDTH_HALF = 29;
    private static final int TILE_HEIGHT_HALF = 15;

    static public Point isoTo2D(Point p) {
        Point tempPt = new Point(0, 0);
        tempPt.x = (2 * p.y + p.x) / 2;
        tempPt.y = (2 * p.y - p.x) / 2;
        return tempPt;
    }


    static public Point TwoDToIsoTexture(Point p, int tile_width, int tile_height, int size) {
        Point tempPt = new Point(0, 0);
        tempPt.x = p.x * 58 / 2 - (p.y - size) * 58 / 2;
        tempPt.y = p.x * 30 / 2 + (p.y - size) * 30 / 2;
        tempPt.x = tempPt.x - tile_width / 2;
        tempPt.y = tempPt.y + size * 30 - tile_height;
        return tempPt;
    }


    static public Point getTileCoordinates(Point pt) {
        Point tempPt = new Point(0, 0);
        tempPt.x = ((pt.y / TILE_HEIGHT_HALF) + (pt.x / TILE_WIDTH_HALF)) / 2;
        tempPt.y = ((pt.y / TILE_HEIGHT_HALF) - (pt.x / TILE_WIDTH_HALF)) / 2;
        return (tempPt);
    }

    static public Point getPointTile(Point pt) {
        Point tempPt = new Point(0, 0);
        tempPt.x = (float) Math.floor(pt.x / TILE_HEIGHT_HALF * 2);
        tempPt.y = (float) Math.floor(pt.y / TILE_WIDTH_HALF * 2);
        return tempPt;
    }

    static public Case getCase(Point e, City city) {
        float x = e.getX() - GameWindow.getCameraPosx();
        float y = e.getY() - GameWindow.getCameraPosy() - GameLayout.TOOLBAR_HEIGHT;//- 40f;

        double x2 = IsometricRender.getTileCoordinates(new Point(x, y)).x;
        double y2 = IsometricRender.getTileCoordinates(new Point(x, y)).y + 1;

        for (int i = 5; i >= 0; i--) {
            Case t = city.getMap().getCase((int) x2 + i, (int) y2 + i);
            if (t != null) {
                if (t.getZlvl() == i || (t.getTerrain() instanceof Elevation && ((Elevation) t.getTerrain()).getSizeZ() == i)) {
                    return t;
                }
            }
        }
        return new Case((int) x2, (int) y2, null, null);
    }


}
