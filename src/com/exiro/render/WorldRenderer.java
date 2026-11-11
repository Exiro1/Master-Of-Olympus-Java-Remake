package com.exiro.render;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.object.BaseObject;
import com.exiro.object.Case;
import com.exiro.object.ObjectClass;
import com.exiro.render.building.BuildingRenderers;
import com.exiro.sprite.Sprite;
import com.exiro.terrainList.Elevation;
import com.exiro.terrainList.Terrain;
import com.exiro.terrainList.Water;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Draws the visible part of the city in back-to-front order.
 *
 * <h2>Why the old order went wrong</h2>
 * Tiles were sorted by depth {@code x + y} and each multi-tile object was drawn in full when the
 * loop reached its main case (the left corner of its footprint). One depth value cannot be right
 * for a whole rectangle: for a building that is longer in x than in y, tiles behind its far edge
 * get a bigger depth than the anchor and are drawn on top of it; for one longer in y, tiles in
 * front of its near edge get a smaller depth and the building is drawn over them. Separately,
 * flat ground tiles in front of a walker were drawn after it and covered its feet.
 *
 * <h2>What this does instead</h2>
 * <ol>
 *   <li><b>Ground pass.</b> Flat terrain and flat objects (roads, rubble) are drawn first, so
 *   ground can never cover a sprite or a building.</li>
 *   <li><b>Sliced objects.</b> Each multi-tile object is cut into vertical strips half a tile
 *   wide. A strip only overlaps tiles on two neighbouring screen columns (diagonals
 *   {@code x - y}), and it is drawn when the loop reaches the front-most footprint tile among
 *   those two columns. With that anchor, every tile behind the strip has a smaller depth and
 *   every tile in front a bigger one, so a per-tile depth sort is exact again. Strips are drawn
 *   by calling the object's normal {@code Render} with a clip, so no render code had to change.</li>
 *   <li><b>Sprites on a footprint</b> (e.g. a worker standing on its building) are drawn after
 *   the object's last strip, i.e. on top of it, as before.</li>
 * </ol>
 */
public class WorldRenderer {

    /** Half tile width in pixels; must match {@link IsometricRender}. */
    private static final int TILE_HALF_W = 29;
    /** Tile height in pixels. */
    private static final int TILE_H = 30;

    private static final class Slice {
        final ObjectClass obj;
        final int left, right; // screen x relative to the camera; open-ended at the object's edges

        Slice(ObjectClass obj, int left, int right) {
            this.obj = obj;
            this.left = left;
            this.right = right;
        }
    }

    // Per-frame scratch, reused between frames to avoid churn.
    private final IdentityHashMap<ObjectClass, ArrayList<Case>> footprints = new IdentityHashMap<>();
    private final IdentityHashMap<Case, ArrayList<Slice>> slicesAt = new IdentityHashMap<>();
    private final IdentityHashMap<ObjectClass, Case> lastAnchor = new IdentityHashMap<>();
    private final IdentityHashMap<ObjectClass, ArrayList<Sprite>> deferred = new IdentityHashMap<>();

    /**
     * @param visible visible cases, already sorted back to front (by x + y, then x)
     */
    public void render(Graphics g, List<Case> visible, int camX, int camY, int viewW, int viewH) {
        footprints.clear();
        slicesAt.clear();
        lastAnchor.clear();
        deferred.clear();

        // Collect the visible footprint of every non-flat object.
        for (Case c : visible) {
            ObjectClass o = c.getObject();
            if (o != null && !isGround(o))
                footprints.computeIfAbsent(o, k -> new ArrayList<>()).add(c);
        }
        for (Map.Entry<ObjectClass, ArrayList<Case>> e : footprints.entrySet())
            buildSlices(e.getKey(), e.getValue());

        // Pass 1: ground.
        for (Case c : visible) {
            ObjectClass o = c.getObject();
            if (o == null) {
                Terrain t = c.getTerrain();
                if (!(t instanceof Water) && isGround(t))
                    t.Render(g, camX, camY);
            } else if (c.isMainCase() && isGround(o)) {
                draw(o, g, camX, camY);
            }
        }

        // Pass 2: everything with height, back to front.
        Shape baseClip = g.getClip();
        for (Case c : visible) {
            ObjectClass o = c.getObject();
            Terrain t = c.getTerrain();

            if (o == null && !(t instanceof Water) && !isGround(t))
                t.Render(g, camX, camY);

            ArrayList<Slice> slices = slicesAt.get(c);
            if (slices != null) {
                for (Slice s : slices) {
                    int left = s.left == Integer.MIN_VALUE ? 0 : Math.max(0, camX + s.left);
                    int right = s.right == Integer.MAX_VALUE ? viewW : Math.min(viewW, camX + s.right);
                    if (right <= left)
                        continue;
                    g.clipRect(left, 0, right - left, viewH);
                    draw(s.obj, g, camX, camY);
                    g.setClip(baseClip);
                }
            }

            if (t instanceof Elevation && ((Elevation) t).isHasRoad())
                t.Render(g, camX, camY);

            // Sprites standing on an object's footprint wait until the object is fully drawn.
            Case objectLast = o == null ? null : lastAnchor.get(o);
            List<Sprite> sprites = c.getSprites();
            if (objectLast != null && objectLast != c) {
                if (!sprites.isEmpty())
                    deferred.computeIfAbsent(o, k -> new ArrayList<>()).addAll(sprites);
                continue;
            }
            if (objectLast == c) {
                ArrayList<Sprite> waiting = deferred.remove(o);
                if (waiting != null)
                    for (Sprite s : waiting)
                        s.Render(g, camX, camY);
            }
            for (Sprite s : sprites)
                s.Render(g, camX, camY);
        }

        // Objects whose last strip is off screen: still draw their sprites.
        for (ArrayList<Sprite> waiting : deferred.values())
            for (Sprite s : waiting)
                s.Render(g, camX, camY);
    }

    /** Buildings are drawn by their renderer, everything else still draws itself. */
    private static void draw(ObjectClass o, Graphics g, int camX, int camY) {
        if (o instanceof Building)
            BuildingRenderers.render((Building) o, g, camX, camY);
        else
            o.Render(g, camX, camY);
    }

    private void buildSlices(ObjectClass o, ArrayList<Case> tiles) {
        boolean hasMain = false;
        int n = tiles.size();
        int[] xs = new int[n], ys = new int[n];
        for (int i = 0; i < n; i++) {
            Case c = tiles.get(i);
            hasMain |= c.isMainCase();
            xs[i] = c.getxPos();
            ys[i] = c.getyPos();
        }
        if (!hasMain)
            return; // the old renderer only drew objects from a main case; keep that rule

        int[] strips = computeStrips(xs, ys);
        lastAnchor.put(o, tiles.get(strips[0]));
        for (int k = 1; k < strips.length; k += 3)
            addSlice(tiles.get(strips[k]), new Slice(o, strips[k + 1], strips[k + 2]));
    }

    /**
     * Cuts a footprint into vertical strips and picks the tile each strip is drawn with.
     *
     * @param xs tile x of each footprint tile
     * @param ys tile y of each footprint tile (same length)
     * @return {@code [last, a0, left0, right0, a1, left1, right1, ...]} where {@code last} is the
     * index of the front-most tile, {@code ai} the index of strip i's anchor tile, and
     * {@code lefti/righti} its screen x range relative to the camera. The first strip starts at
     * {@link Integer#MIN_VALUE} and the last one ends at {@link Integer#MAX_VALUE}, so any part of
     * the image that overhangs the footprint is still drawn.
     */
    static int[] computeStrips(int[] xs, int[] ys) {
        int n = xs.length;
        int minD = Integer.MAX_VALUE, maxD = Integer.MIN_VALUE, last = 0;
        for (int i = 0; i < n; i++) {
            int d = xs[i] - ys[i];
            minD = Math.min(minD, d);
            maxD = Math.max(maxD, d);
            if (isInFront(xs[i], ys[i], xs[last], ys[last]))
                last = i;
        }
        if (minD == maxD) // single screen column (e.g. a one-tile object): draw it whole
            return new int[]{last, last, Integer.MIN_VALUE, Integer.MAX_VALUE};

        // Front-most tile of each screen column (diagonal x - y).
        int[] front = new int[maxD - minD + 1];
        java.util.Arrays.fill(front, -1);
        for (int i = 0; i < n; i++) {
            int col = xs[i] - ys[i] - minD;
            if (front[col] < 0 || isInFront(xs[i], ys[i], xs[front[col]], ys[front[col]]))
                front[col] = i;
        }

        // Strip j lies between the centres of columns j and j+1, so it only overlaps tiles of
        // those two columns. Its anchor is the front-most footprint tile of the two.
        // Neighbouring strips with the same anchor are merged.
        int[] out = new int[1 + 3 * (maxD - minD + 2)];
        out[0] = last;
        int len = 1;
        int runAnchor = -1, runLeft = Integer.MIN_VALUE;
        for (int j = minD - 1; j <= maxD; j++) {
            int a = j >= minD ? front[j - minD] : -1;
            int b = j + 1 <= maxD ? front[j + 1 - minD] : -1;
            int anchor = a < 0 ? b : b < 0 ? a : (isInFront(xs[a], ys[a], xs[b], ys[b]) ? a : b);
            if (anchor < 0)
                anchor = runAnchor; // gap in a non-rectangular footprint
            if (runAnchor < 0) {
                runAnchor = anchor;
            } else if (anchor != runAnchor) {
                int boundary = j * TILE_HALF_W;
                out[len++] = runAnchor;
                out[len++] = runLeft;
                out[len++] = boundary;
                runAnchor = anchor;
                runLeft = boundary;
            }
        }
        out[len++] = runAnchor;
        out[len++] = runLeft;
        out[len++] = Integer.MAX_VALUE;
        return java.util.Arrays.copyOf(out, len);
    }

    private void addSlice(Case anchor, Slice s) {
        slicesAt.computeIfAbsent(anchor, k -> new ArrayList<>()).add(s);
    }

    /** Same order as the tile sort: larger x + y is in front, ties broken by larger x. */
    static boolean isInFront(int ax, int ay, int bx, int by) {
        int da = ax + ay, db = bx + by;
        return da != db ? da > db : ax > bx;
    }

    /** Flat things that never stick out of their own tile diamond. */
    static boolean isGround(BaseObject o) {
        boolean floor;
        if (o instanceof Terrain)
            floor = ((Terrain) o).isFloor();
        else if (o instanceof Construction)
            floor = ((Construction) o).isFloor();
        else
            return false;
        return floor && o.getHeight() <= TILE_H * Math.max(1, o.getSize()) + 1;
    }
}
