package com.exiro.ai;

import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;

public abstract class AI {


    public abstract Path roaming(City city, int len, int freeState, Case startCase);

    public static Path goTo(City city, Case start, Case end, int free) {
        return city.getPathManager().getPathTo(start, end, free);
    }

    public static Path goTo(City city, Case start, Case end, int free, boolean straight) {
        return city.getPathManager().getPathTo(start, end, free, straight);
    }
}
