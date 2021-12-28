package com.exiro.terrainGenerator;

import com.exiro.sprite.Direction;

public enum ElevationType {


    E1(0b01010101,0b00010100,20,1,Direction.NORD),
    E2(0b01010101,0b00010101,21,1,Direction.NORD),
    E3(0b01010101,0b00000101,22,1,Direction.NORD),
    E4(0b01010101,0b01000101,23,1,Direction.NORD),
    E5(0b01010101,0b01000001,24,1,Direction.NORD),
    E6(0b01010101,0b01010001,25,1,Direction.NORD),
    E7(0b01010101,0b01010000,26,1,Direction.NORD),
    E8(0b01010101,0b01010100,27,1,Direction.NORD),
    E9(0b11111111,0b11110111,28,1,Direction.NORD),
    E10(0b11111111,0b11111101,29,1,Direction.NORD),
    E11(0b11111111,0b01111111,30,1,Direction.NORD),
    E12(0b11111111,0b11011111,31,1,Direction.NORD),

    E13(0b11111111,0b11100000,80,1,Direction.NORD),
    E14(0b11111111,0b00001110,76,1,Direction.NORD),
    E15(0b11111111,0b10000011,82,1,Direction.NORD),
    E16(0b11111111,0b00111000,78,1,Direction.NORD),

    NONE(0b11111111,0b11111111,0,1,Direction.NORD),
    ERROR(0b11111111,0b00000000,0,1,Direction.NORD);

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
