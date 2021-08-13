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
    ONLY_ACTIVE_ROAD(0b1), ALL_ROAD(0b11), BLOCKING_ROAD(0b10), BUILDABLE(0b100), NON_BLOCKING(0b1001), BLOCKED(0b00000), BUILDABLE_ROAD(0b10000);

    public final int i;

    FreeState(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}
