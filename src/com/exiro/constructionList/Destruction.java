package com.exiro.constructionList;

import com.exiro.object.City;

import com.exiro.object.ObjectType;

public class Destruction extends Construction {


    public Destruction(City city) {
        super(false, ObjectType.ERASE, null, 0, 0, 0, 0, 1, 1, 0, city, false, true);
    }

    @Override
    public void process(double deltatime, int deltaDays) {

    }
}
