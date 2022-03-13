package com.exiro.terrainGenerator;

public class CoastSelector {



    public static CoastType getCoastType(int b){
        for(CoastType type : CoastType.values()){
            int masked = b & type.mask;
            if(masked == type.comp){
                return type;
            }
        }
        return CoastType.NONE;
    }




}

