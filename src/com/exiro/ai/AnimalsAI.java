package com.exiro.ai;

import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;

import java.util.ArrayList;
import java.util.Random;

public class AnimalsAI extends AI {


    @Override
    public Path roaming(City city, int len, int freeState, Case startCase) {
        ArrayList<Case> path = new ArrayList<>();
        path.add(startCase);
        int currlen = 0;
        Random r = new Random();
        Case currCase = startCase;
        int dx = 0, dy = 0;
        while (currlen <= len) {
            int nbr = currCase.getNeighbourCount(freeState, city.getMap());
            boolean ok = false;
            for (int i = 0; i < nbr; i++) {
                if (!path.contains(currCase.getNeighbourIndex(freeState, i, city.getMap()))) {
                    currCase = currCase.getNeighbourIndex(freeState, r.nextInt(nbr), city.getMap());
                    path.add(currCase);
                    currlen++;
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                break;
            }
        }
        return new Path(path);
    }


}
