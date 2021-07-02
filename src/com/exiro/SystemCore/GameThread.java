package com.exiro.SystemCore;

import com.exiro.BuildingList.Building;
import com.exiro.BuildingList.BuildingType;
import com.exiro.ConstructionList.Road;
import com.exiro.MoveRelated.RoadMap;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Object.Player;
import com.exiro.Render.GameFrame;
import com.exiro.Render.MouseManager;

public class GameThread implements Runnable {


    static public float deltaTime;
    final boolean continu = true;
    float fps = 144;
    final long deltaTimeResearched = (long) ((1f / 144f) * 1000f);
    double timeSinceLastUpdate = 0;
    private final Player p;
    private int currentCity;
    private final GameFrame frame;
    private final GameManager gm;

    public GameThread(GameManager gm) {
        this.p = gm.player;
        this.frame = gm.frame;
        this.gm = gm;
    }

    @Override
    public void run() {
        try {
            gameThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gameThread() throws InterruptedException {
        System.out.println("d : " + deltaTimeResearched);
        while (continu) {
            long startTime = System.currentTimeMillis();
            timeSinceLastUpdate = timeSinceLastUpdate + deltaTime;
            if (timeSinceLastUpdate > 1) {

                for (City c : p.getPlayerCities()) {
                    manageBuilding(c, timeSinceLastUpdate);
                }
                timeSinceLastUpdate = 0;
            }

            for (City c : p.getPlayerCities()) {
                manageSprite(c, deltaTime);
            }


            try {
                manageBuild();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            gm.menuBar.repaint();
            gm.GameView.repaint();
            gm.frame.getGi().repaint();
            gm.frame.getIt().repaint();
            Thread.sleep(deltaTimeResearched);

            float a = System.currentTimeMillis() - startTime;
            deltaTime = a / 1000.0f;

        }
    }


    public void manageBuild() {

        if (MouseManager.pressing) {

        } else {

        }

    }

    public void manageSprite(City c, double deltaTime) {
        synchronized (c.getBuildings()) {

            for (Building b : c.getBuildings()) {
                b.processSprite(deltaTime);
            }
        }
    }

    public void manageBuilding(City c, double delaTime) {

        synchronized (c.getBuildings()) {

            for (Building b : c.getBuildings()) {
                if (b.isActive()) {
                    b.process(delaTime);
                    b.populate(delaTime);
                } else {

                }
            }
        }
        synchronized (c.getPathManager().getRoads()) {
            for (Road r : c.getPathManager().getRoads()) {
                r.setActive(p.getPlayerCities().get(currentCity).getPathManager().isReachable(p.getPlayerCities().get(currentCity).getMap().getCase(r.getxPos(), r.getYpos()), p.getPlayerCities().get(currentCity).getMap().getStartCase(), RoadMap.FreeState.ALL_ROAD));
            }
        }

        synchronized (c.getObj()) {
            c.getObj().parallelStream().forEach(this::activate);
        }
        c.getBuildingManager().updateWorker();
    }

    public void activate(ObjectClass o) {
        if (o.getBuildingType() == BuildingType.ROAD)
            return;
        o.setActive(o.getAccess().size() != 0);
    }


}
