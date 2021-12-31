package com.exiro.moveRelated;

/**
 * Creates nodes and neighbours from a 2d grid. Each point in the map has an
 * integer value that specifies the cost of crossing that point. If this value
 * is negative, the point is unreachable.
 * <p>
 * If diagonal movement is allowed, the Chebyshev distance is used, else
 * Manhattan distance is used.
 *
 * @author Ben Ruijl
 */

public enum FreeState {
    EMPTY_TERRAIN(0b1),ACTIVE_ROAD(0b10),BLOCKING_ROAD(0b100),MEADOW(0b1000),WATER(0b10000),BUILDABLE_ELEVATION(0b100000),WALKABLE_CASE(0b1000000),BLOCKED(0b0),
    ALL_ROAD(0b110),BUILDABLE(0b01001),BUILDABLE_ROAD(0b101001),NON_BLOCKING(0b1101111);


    public final int i;

    FreeState(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}
