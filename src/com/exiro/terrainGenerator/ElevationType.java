package com.exiro.terrainGenerator;

import com.exiro.sprite.Direction;

public enum ElevationType {


    E1(0b01010101, 0b00010100, 20, 1, Direction.NORTH),
    E2(0b01010101, 0b00010101, 21, 1, Direction.NORTH_EAST),
    E3(0b01010101, 0b00000101, 22, 1, Direction.EAST),
    E4(0b01010101, 0b01000101, 23, 1, Direction.SOUTH_EAST),
    E5(0b01010101, 0b01000001, 24, 1, Direction.SOUTH),
    E6(0b01010101, 0b01010001, 25, 1, Direction.SOUTH_WEST),
    E7(0b01010101, 0b01010000, 26, 1, Direction.WEST),
    E8(0b01010101, 0b01010100, 27, 1, Direction.NORTH_WEST),
    E9(0b11111111, 0b11110111, 28, 1, Direction.SOUTH),
    E10(0b11111111, 0b11111101, 29, 1, Direction.WEST),
    E11(0b11111111, 0b01111111, 30, 1, Direction.NORTH),
    E12(0b11111111, 0b11011111, 31, 1, Direction.EAST),

    E13(0b11111111, 0b11100000, 80, 1, Direction.NORTH),
    E14(0b11111111, 0b00001110, 76, 1, Direction.NORTH),
    E15(0b11111111, 0b10000011, 82, 1, Direction.NORTH),
    E16(0b11111111, 0b00111000, 78, 1, Direction.NORTH),

    NONE(0b11111111, 0b11111111, 0, 1, Direction.NORTH),
    ERROR(0b11111111, 0b00000000, 0, 1, Direction.NORTH);

    int mask, comp, nbr, id;
    Direction dir;

    ElevationType(int mask, int comp, int id, int nbr, Direction dir) {
        this.comp = comp;
        this.mask = mask;
        this.id = id;
        this.nbr = nbr;
        this.dir = dir;
    }

    public static ElevationType getElevationType(int b){
        for(ElevationType type : ElevationType.values()){
            int masked = b & type.mask;
            if(masked == type.comp){
                return type;
            }
        }
        return ElevationType.ERROR;
    }

    public int getMask() {
        return mask;
    }

    public int getComp() {
        return comp;
    }

    public int getNbr() {
        return nbr;
    }

    public int getId() {
        return id;
    }

    public Direction getDir() {
        return dir;
    }
}
