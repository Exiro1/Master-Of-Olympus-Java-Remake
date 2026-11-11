package com.exiro.render.building;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.House;
import com.exiro.buildingList.agriculture.Farm;
import com.exiro.buildingList.agriculture.GoatBuildingBind;
import com.exiro.buildingList.agriculture.SheepBuildingBind;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.buildingList.stockage.Granary;
import com.exiro.buildingList.stockage.StoreHouse;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.BuildingSprite;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Picks the renderer of a building by its class, falling back to the closest registered
 * superclass, then to {@link #DEFAULT} (image, plus animations while working).
 * A new building that looks standard needs nothing here; one with special drawing registers
 * a renderer in the static block.
 */
public final class BuildingRenderers {

    /** Draws worker counts and image bounds on every building. */
    public static final boolean DEBUG = false;

    private static final Map<Class<?>, BuildingRenderer<?>> RENDERERS = new HashMap<>();

    /** Building image, then its animations when it has workers. */
    public static final BuildingRenderer<Building> DEFAULT = (b, g, camX, camY) -> {
        drawBase(b, g, camX, camY);
        if (b.isWorking())
            drawSprites(b, g, camX, camY);
    };

    /** For buildings that are not drawn themselves (agora frame, animal binds). */
    public static final BuildingRenderer<Building> NOTHING = (b, g, camX, camY) -> {
    };

    static {
        register(House.class, HouseRenderer::render);
        register(Granary.class, GranaryRenderer::render);
        register(StoreHouse.class, StoreHouseRenderer::render);
        register(Farm.class, FarmRenderer::render);
        register(AgoraShopBuilding.class, AgoraShopRenderer::render);
        register(Agora.class, NOTHING);
        register(GoatBuildingBind.class, NOTHING);
        register(SheepBuildingBind.class, NOTHING);
    }

    private BuildingRenderers() {
    }

    public static <T extends Building> void register(Class<T> type, BuildingRenderer<? super T> renderer) {
        RENDERERS.put(type, renderer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void render(Building b, Graphics g, int camX, int camY) {
        BuildingRenderer r = forClass(b.getClass());
        r.render(b, g, camX, camY);
    }

    private static BuildingRenderer<?> forClass(Class<?> c) {
        BuildingRenderer<?> found = RENDERERS.get(c);
        if (found != null)
            return found;
        for (Class<?> k = c.getSuperclass(); k != null && Building.class.isAssignableFrom(k); k = k.getSuperclass()) {
            found = RENDERERS.get(k);
            if (found != null)
                break;
        }
        if (found == null)
            found = DEFAULT;
        RENDERERS.put(c, found); // remember the lookup for this class
        return found;
    }

    /**
     * Draws the building image at its main case, raised by that case's level.
     *
     * @return the image's top-left corner, relative to the camera
     */
    public static Point drawBase(Building b, Graphics g, int camX, int camY) {
        int lvl = b.getMainCase().getZlvl();
        Point p = IsometricRender.TwoDToIsoTexture(new Point(b.getxPos() - lvl, b.getyPos() - lvl), b.getWidth(), b.getHeight(), b.getSize());
        g.drawImage(b.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        if (DEBUG) {
            g.drawString(b.getPop() + "/" + b.getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);
            g.setColor(Color.RED);
            g.drawRect(camX + (int) p.getX(), camY + (int) p.getY(), b.getWidth(), b.getHeight());
            g.setColor(Color.BLACK);
        }
        return p;
    }

    /** Every building sprite (moving sprites are drawn by the world renderer). */
    public static void drawSprites(Building b, Graphics g, int camX, int camY) {
        for (BuildingSprite s : b.getBuildingSprites())
            s.Render(g, camX, camY);
    }
}
