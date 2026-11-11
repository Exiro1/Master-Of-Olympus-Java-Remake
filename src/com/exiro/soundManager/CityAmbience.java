package com.exiro.soundManager;

import com.exiro.fileManager.SoundLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.render.layout.GameWindow;
import com.exiro.systemCore.GameManager;

import java.util.Random;

/**
 * Picks ambient sounds from the city on screen, around the centre of the view: a tile is drawn
 * from a normal distribution centred on the screen centre, so nearby buildings are heard most
 * often and the far edges of the view only now and then. Buildings are preferred over terrain.
 *
 * It reads the current city on every pick, so switching cities switches the ambience too.
 */
public class CityAmbience implements SoundManager.AmbientPicker {

    /** Spread of the picks around the screen centre, in tiles. */
    private static final double SPREAD_TILES = 7;
    private static final int TRIES = 8;
    /** Chance of playing terrain when no building was found near the centre. */
    private static final double TERRAIN_CHANCE = 0.5;

    private final GameManager gm;

    public CityAmbience(GameManager gm) {
        this.gm = gm;
    }

    /** World-pixel position of the centre of the game view, for {@link SoundManager#updateListener}. */
    public float[] viewCentre() {
        GameWindow view = gm.getGameView();
        int w = view != null ? view.getWidth() : 0, h = view != null ? view.getHeight() : 0;
        return new float[]{w / 2f - GameWindow.getCameraPosx(), h / 2f - GameWindow.getCameraPosy()};
    }

    @Override
    public SoundManager.AmbientPick pick(Random random) {
        City city = gm.getCurrentCity();
        if (city == null)
            return null;
        float[] c = viewCentre();
        // Inverse of SoundManager.tileToWorld.
        double cx = (c[1] / 15.0 + c[0] / 29.0) / 2.0;
        double cy = (c[1] / 15.0 - c[0] / 29.0) / 2.0;

        Case terrain = null;
        for (int i = 0; i < TRIES; i++) {
            int x = (int) Math.round(cx + random.nextGaussian() * SPREAD_TILES);
            int y = (int) Math.round(cy + random.nextGaussian() * SPREAD_TILES);
            Case k = city.getMap().getCase(x, y);
            if (k == null)
                continue;
            ObjectClass o = k.getObject();
            if (o != null && hasSound(o.getSoundCategory()))
                return new SoundManager.AmbientPick(o.getSoundCategory(), x, y);
            if (terrain == null && k.getTerrain() != null && hasSound(k.getTerrain().getSoundCategory()))
                terrain = k;
        }
        if (terrain != null && random.nextDouble() < TERRAIN_CHANCE)
            return new SoundManager.AmbientPick(terrain.getTerrain().getSoundCategory(), terrain.getxPos(), terrain.getyPos());
        return null;
    }

    private static boolean hasSound(SoundLoader.SoundCategory c) {
        return c != null && c != SoundLoader.SoundCategory.NULL && !SoundLoader.getSoundPaths(c).isEmpty();
    }
}
