package com.exiro.sprite;

public enum Direction {

    // spriteIndex: position of this direction in the 8 images of one animation frame
    SOUTH(3), SOUTH_EAST(2), EAST(1), NORTH_EAST(0), NORTH(7), NORTH_WEST(6), WEST(5), SOUTH_WEST(4);

    private final int spriteIndex;

    Direction(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
