package com.exiro.systemCore;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.object.City;
import com.exiro.utils.Priority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildingManager {


    private int lastPop;
    private final Map<BuildingCategory, Priority> priorityMap;
    private Map<BuildingCategory, Priority> lastPriorityMap;
    private final City city;
    private int WorkforceNeeded;
    private int unemployed;
    private int lastActiveNbr;


    public BuildingManager(City city) {
        this.city = city;
        priorityMap = new HashMap<>();
        priorityMap.put(BuildingCategory.ARMY, Priority.MEDIUM);
        priorityMap.put(BuildingCategory.FOOD, Priority.MEDIUM);
        priorityMap.put(BuildingCategory.MARKET, Priority.MEDIUM);
        priorityMap.put(BuildingCategory.SCIENCE, Priority.MEDIUM);
        priorityMap.put(BuildingCategory.SECURITY, Priority.MEDIUM);
        priorityMap.put(BuildingCategory.STOCKAGE, Priority.MEDIUM);
    }

    //peut etre appeler la methode que quand on change qlq chose au lieu de le faire a chaque seconde?
    public void updateWorker() {
        int pop = city.getPopulation();

        if (pop == lastPop && priorityMap.equals(lastPriorityMap) && lastActiveNbr == city.getActiveBuilding())
            return;//evite de tout recalculer sii rien a changer

        int popR = pop;
        ArrayList<Building> buildings = city.getBuildings();
        double high = 0, normal = 0, low = 0;

        for (Building b : buildings) {
            if (!b.isActive())
                continue;
            if (priorityMap.get(b.getCategory()) == Priority.LOW)
                low = low + b.getPopMax();
            if (priorityMap.get(b.getCategory()) == Priority.HIGH)
                high = high + b.getPopMax();
            if (priorityMap.get(b.getCategory()) == Priority.MEDIUM)
                normal = normal + b.getPopMax();
        }
        double rHigh = 0, rNormal = 0, rLow = 0;
        WorkforceNeeded = (int) (high + normal + low);
        if (high != 0)
            rHigh = (pop / high) > 1 ? 1 : pop / high;
        if (normal != 0)
            rNormal = ((pop - high) / normal) > 1 ? 1 : (pop - high) / normal;
        if (low != 0)
            rLow = ((pop - high - normal) / low) > 1 ? 1 : (pop - high - normal) / low;

        for (Building b : buildings) {
            if (priorityMap.containsKey(b.getCategory()))
                b.setPop(0);
        }
        while (popR > pop - getWorkforceNeeded() || popR == 0) {
            for (Building b : buildings) {
                if (!b.isActive())
                    continue;
                if (popR <= 0)
                    break;

                if (priorityMap.get(b.getCategory()) == Priority.HIGH && b.getPopMax() > b.getPop() && Math.round(b.getPopMax() * rHigh) >= b.getPop()) {
                    popR--;
                    b.setPop(b.getPop() + 1);
                } else if (priorityMap.get(b.getCategory()) == Priority.MEDIUM && b.getPopMax() > b.getPop() && Math.round(b.getPopMax() * rNormal) >= b.getPop()) {
                    popR--;
                    b.setPop(b.getPop() + 1);
                } else if (priorityMap.get(b.getCategory()) == Priority.LOW && b.getPopMax() > b.getPop() && Math.round(b.getPopMax() * rLow) >= b.getPop()) {
                    popR--;
                    b.setPop(b.getPop() + 1);
                }
            }
            if (popR <= 0)
                break;

        }
        System.out.println("reste " + popR + " gens qui ne travaille pas");
        unemployed = popR;
        lastActiveNbr = city.getActiveBuilding();
        lastPop = city.getPopulation();
        lastPriorityMap = priorityMap;

    }

    public int getWorkforceNeeded() {
        return WorkforceNeeded;
    }

    public void setWorkforceNeeded(int workforceNeeded) {
        WorkforceNeeded = workforceNeeded;
    }

    public int getUnemployed() {
        return unemployed;
    }

    public void setUnemployed(int unemployed) {
        this.unemployed = unemployed;
    }


}
