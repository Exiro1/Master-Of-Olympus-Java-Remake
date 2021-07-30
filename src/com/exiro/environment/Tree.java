package com.exiro.environment;

import com.exiro.object.City;
import com.exiro.object.ObjectType;

public class Tree extends Environment {


    public Tree(int xpos, int ypos, int id, City c) {
        super(ObjectType.TREE, false, xpos, ypos, c, false);
        this.setLocalID(id);
        updateImg();
    }


}
