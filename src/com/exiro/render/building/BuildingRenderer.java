package com.exiro.render.building;

import com.exiro.buildingList.Building;

import java.awt.*;

/**
 * Draws one kind of building. Buildings hold game state only; how they look lives here, so the
 * model can run without any drawing code (e.g. on a server).
 */
@FunctionalInterface
public interface BuildingRenderer<T extends Building> {

    void render(T building, Graphics g, int camX, int camY);
}
