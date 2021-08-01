package com.exiro.ai;

import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;

import java.util.ArrayList;
import java.util.Random;

public class DeliveryAI extends AI {


    public DeliveryAI() {

    }


    @Override
    public Path roaming(City city, int len, int freeState, Case startCase) {
        ArrayList<Case> path = new ArrayList<>();
        path.add(startCase);
        int currlen = 0;
        Random r = new Random();
        Case lastcase = startCase;
        Case currCase = startCase;
        int dx = 0, dy = 0;
        while (currlen <= len) {

            dx = currCase.getxPos() - lastcase.getxPos();
            dy = currCase.getyPos() - lastcase.getyPos();
            lastcase = currCase;
            int nbr = currCase.getNeighbourCount(freeState, city.getMap());
            if (nbr == 0)
                break;
            if (nbr == 1) {
                currCase = currCase.getNeighbourIndex(freeState, 0, city.getMap());
            } else if (nbr == 2) {
                if (!path.contains(currCase.getNeighbourIndex(freeState, 0, city.getMap())) && !path.contains(currCase.getNeighbourIndex(freeState, 1, city.getMap()))) {
                    currCase = currCase.getNeighbourIndex(freeState, r.nextInt(nbr), city.getMap());
                } else if (path.get(path.size() - 2) == currCase.getNeighbourIndex(freeState, 0, city.getMap())) {
                    currCase = currCase.getNeighbourIndex(freeState, 1, city.getMap());
                } else {
                    currCase = currCase.getNeighbourIndex(freeState, 0, city.getMap());
                }
            } else {
                currCase = currCase.getNeighbourIndex(freeState, r.nextInt(nbr), city.getMap());
            }
            path.add(currCase);
            currlen++;

        }
        return new Path(path);
    }

}
