package com.exiro.terrainGenerator;

import com.exiro.sprite.Direction;

public enum CoastType {
    C1(0b01010101, 0b00000001, 183, 4,Direction.NORD_OUEST),
    C3(0b01010101, 0b00000100, 179, 4,Direction.SUD_OUEST),
    C4(0b01010101, 0b00010000, 175, 4,Direction.SUD_EST),
    C2(0b01010101, 0b01000000, 171, 4,Direction.NORD_EST),
    C8(0b11111111, 0b00000010, 195, 4,Direction.OUEST),
    C7(0b11111111, 0b00001000, 191, 4,Direction.SUD),
    C6(0b11111111, 0b00100000, 187, 4,Direction.EST),
    C5(0b11111111, 0b10000000, 199, 4,Direction.NORD_EST),
    C10(0b01110101, 0b00000101, 215, 1,Direction.EST),
    C9(0b11010101, 0b00010100, 214, 1,Direction.SUD),
    C12(0b01010111, 0b01010000, 213, 1,Direction.OUEST),
    C11(0b01011101, 0b01000001, 216, 1,Direction.NORD),
    NONE(0b11111111, 0b00000000, 162, 1,Direction.NORD);

    int mask, comp, nbr, id;
    Direction dir;

    CoastType(int mask, int comp, int id, int nbr, Direction dir) {
        this.comp = comp;
        this.mask = mask;
        this.id = id;
        this.nbr = nbr;
        this.dir = dir;
    }

    public static CoastType getCoastType(int b){
        for(CoastType type : CoastType.values()){
            int masked = b & type.mask;
            if(masked == type.comp){
                return type;
            }
        }
        return CoastType.NONE;
    }

    public Direction getDir() {
        return dir;
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
}
