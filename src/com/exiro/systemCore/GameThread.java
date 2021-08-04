package com.exiro.systemCore;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.object.Player;
import com.exiro.render.GameFrame;
import com.exiro.render.MouseManager;
import com.exiro.terrainList.Terrain;

public class GameThread implements Runnable {


    static public float deltaTime;
    final boolean continu = true;
    float fps = 144;
    final long deltaTimeResearched = (long) ((1f / 144f) * 1000f);
    double timeSinceLastUpdateBuilding = 0;
    double timeSinceLastUpdateConstruct = 0;
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
            timeSinceLastUpdateBuilding = timeSinceLastUpdateBuilding + deltaTime;
            timeSinceLastUpdateConstruct = timeSinceLastUpdateConstruct + deltaTime;

            if (timeSinceLastUpdateBuilding > 1) {

                for (City c : p.getPlayerCities()) {
                    manageBuilding(c, timeSinceLastUpdateBuilding);
                }
                timeSinceLastUpdateBuilding = 0;
            }
            if (timeSinceLastUpdateConstruct > 3) {

                for (City c : p.getPlayerCities()) {
                    manageConstruction(c, timeSinceLastUpdateConstruct);
                }
                timeSinceLastUpdateConstruct = 0;
            }
            for (City c : p.getPlayerCities()) {
                manageSprite(c, deltaTime);
                manageTerrain(c, deltaTime);
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
            deltaTime = Math.min(a / 1000.0f, deltaTimeResearched / 1000.0f);

        }
    }


    public void manageBuild() {

        if (MouseManager.pressing) {

        } else {

        }

    }

    public void manageTerrain(City c, double deltaTime) {
        synchronized (c.getTerrain()) {
            for (Terrain b : c.getTerrain()) {
                b.process(deltaTime);
            }
        }
    }

    public void manageSprite(City c, double deltaTime) {
        synchronized (c.getBuildings()) {

            for (Building b : c.getBuildings()) {
                b.processSprite(deltaTime);
            }
        }
    }

    public void manageConstruction(City c, double delaTime) {
        synchronized (c.getConstructions()) {
            for (Construction construction : c.getConstructions()) {
                construction.process(delaTime);
            }
        }
    }

    public void manageBuilding(City c, double delaTime) {

        synchronized (c.getBuildings()) {

            for (Building b : c.getBuildings()) {
                b.process(delaTime);
                if (b.isActive()) {
                    b.populate(delaTime);
                } else {

                }
            }
            c.deleteQueue();
        }
        synchronized (c.getPathManager().getRoads()) {
            for (Road r : c.getPathManager().getRoads()) {
                r.setActive(p.getPlayerCities().get(currentCity).getPathManager().isReachable(p.getPlayerCities().get(currentCity).getMap().getCase(r.getxPos(), r.getYpos()), p.getPlayerCities().get(currentCity).getMap().getStartCase(), FreeState.ALL_ROAD.i));
            }
        }

        synchronized (c.getObj()) {
            c.getObj().parallelStream().forEach(this::activate);
        }
        c.getBuildingManager().updateWorker();
    }

    public void activate(ObjectClass o) {
        if (o.getBuildingType() == ObjectType.ROAD)
            return;
        o.setActive(o.getAccess().size() != 0);
    }


}
