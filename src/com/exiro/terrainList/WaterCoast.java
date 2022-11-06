package com.exiro.terrainList;

import com.exiro.fileManager.SoundLoader;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Direction;
import com.exiro.terrainGenerator.CoastType;


public class WaterCoast extends Terrain {

    boolean angle;
    Direction direction;
    int number;

    public WaterCoast(int xpos, int ypos, boolean angle, Direction direction, int number, City city) {
        super(ObjectType.WATERTCOAST, false, xpos, ypos, city, true, false, true, 1);
        this.angle = angle;
        this.direction = direction;
        this.number = number;
        this.setLocalID(getIDfromDir(direction, number));
        updateImg();

    }

    public WaterCoast(int xpos, int ypos, CoastType type, int nbr,City city) {
        super(ObjectType.WATERTCOAST, false, xpos, ypos, city, true, false, true, 1);
        this.setLocalID(type.getId() + Math.min(type.getNbr() - 1, nbr));
        direction = type.getDir();
        angle = direction != Direction.SOUTH_WEST && direction != Direction.SOUTH_EAST && direction != Direction.NORTH_EAST && direction != Direction.NORTH_WEST;
        updateImg();
    }

    public int getIDfromDir(Direction dir, int number) {
        int i = 0;
        switch (dir) {
            case SOUTH:
                i = 191;
                break;
            case SOUTH_EAST:
                i = 175;
                break;
            case EAST:
                i = 187;
                break;
            case NORTH_EAST:
                i = 171;
                break;
            case NORTH:
                i = 199;
                break;
            case NORTH_WEST:
                i = 183;
                break;
            case WEST:
                i = 195;
                break;
            case SOUTH_WEST:
                i = 179;
                break;
        }
        i += number;
        return i;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {

    }

    public boolean isAngle() {
        return angle;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.WATER;
    }
}
