package com.exiro.constructionList;

import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

public class Destruction extends Construction {


    public Destruction() {
        super(false, ObjectType.ERASE, null, 0, 0, 0, 0, 1, 1, 0, GameManager.currentCity, false, true);
    }

    @Override
    public void process(double deltatime) {

    }
}
