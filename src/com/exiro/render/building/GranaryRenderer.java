package com.exiro.render.building;

import com.exiro.buildingList.stockage.Granary;
import com.exiro.sprite.BuildingSprite;

import java.awt.*;
import java.util.List;

final class GranaryRenderer {

    private GranaryRenderer() {
    }

    /** Sprite 0 is the working animation; the others are the stocked goods, always shown. */
    static void render(Granary b, Graphics g, int camX, int camY) {
        BuildingRenderers.drawBase(b, g, camX, camY);
        List<BuildingSprite> sprites = b.getBuildingSprites();
        for (int i = 0; i < sprites.size(); i++) {
            if (i > 0 || b.isWorking())
                sprites.get(i).Render(g, camX, camY);
        }
    }
}
