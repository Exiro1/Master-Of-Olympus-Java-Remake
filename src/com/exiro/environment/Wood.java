package com.exiro.environment;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;

import java.util.ArrayList;

public class Wood {

    private final int state;   //0 =normal , 1 = coupé
    private final float durability; //pour la coupe
    private final boolean isCutting;


    public Wood(City city, ArrayList<Case> cases, int xPos, int ypos, ObjectType type, float cachet, int state, float durability, boolean isCutting) {

        this.state = state;
        this.durability = durability;
        this.isCutting = isCutting;
    }
}
