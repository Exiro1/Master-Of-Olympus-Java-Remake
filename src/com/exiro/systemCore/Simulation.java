package com.exiro.systemCore;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.*;
import com.exiro.render.layout.GameWindow;
import com.exiro.soundManager.SoundManager;
import com.exiro.sprite.Sprite;
import com.exiro.terrainList.Rock;
import com.exiro.terrainList.Terrain;

import java.util.Random;

/**
 * Game logic. Holds no thread of its own: {@link GameLoop} calls {@link #tick(double)} on the
 * Swing event thread, the same thread that paints and handles mouse/keyboard input, so the city
 * state is only ever touched by one thread and needs no locking.
 */
public class Simulation {

    double timeSinceLastUpdateBuilding = 0;
    double timeSinceLastUpdateConstruct = 0;
    double timeSinceLastUpdateResources = 0;
    int deltaDaysB,deltaDaysC,deltaDaysR;
    private final Player p;
    private int currentCity;
    private final GameManager gm;
    private final SoundManager sm;

    Random ran;
    public Simulation(GameManager gm, SoundManager sm) {
        this.p = gm.player;
        this.gm = gm;
        this.sm = sm;
        ran = new Random();
        gm.setSimulation(this);
    }

    /**
     * Advances the game by one fixed step.
     *
     * @param dt step length in seconds (constant, see {@link GameLoop#TICK})
     */
    public void tick(double dt) {
        timeSinceLastUpdateBuilding = timeSinceLastUpdateBuilding + dt;
        timeSinceLastUpdateConstruct = timeSinceLastUpdateConstruct + dt;
        timeSinceLastUpdateResources = timeSinceLastUpdateResources + dt;

        sm.updateListener(GameWindow.getCameraPosx(),GameWindow.getCameraPosy());

        int deltaDays = gm.timeManager.updateTime(dt);
        deltaDaysB+=deltaDays;
        deltaDaysC+=deltaDays;
        deltaDaysR+=deltaDays;
        if (timeSinceLastUpdateBuilding > 1) {
            sm.updateSources();
            /*
            if(sm.secBeforeNextAmbient == 0)
                sm.playAmbientSound();
            if(sm.secBeforeNextAmbient >= 0)
                sm.secBeforeNextAmbient--;
            else
                if(sm.ambientCount<2)
                    sm.secBeforeNextAmbient = ran.nextInt(3);
            */
            if(SoundManager.secBeforeNextMusic == 0)
                sm.playNewMusic();

            if(SoundManager.secBeforeNextMusic >= 0)
                SoundManager.secBeforeNextMusic--;

            if(sm.secBeforeNextLayer1 == 0)
                sm.playNewLayer1();
            if(sm.secBeforeNextLayer1 >= 0)
                sm.secBeforeNextLayer1--;

            for (City c : p.getPlayerCities()) {
                manageBuilding(c, timeSinceLastUpdateBuilding,deltaDaysB);
            }
            timeSinceLastUpdateBuilding = 0;
            deltaDaysB=0;
        }
        if (timeSinceLastUpdateConstruct > 3) {

            for (City c : p.getPlayerCities()) {
                manageConstruction(c, timeSinceLastUpdateConstruct, deltaDaysC);
                c.getResourceManager().process();
            }

            timeSinceLastUpdateConstruct = 0;
            deltaDaysC=0;
        }
        if (timeSinceLastUpdateResources > 30) {

            for (City c : p.getPlayerCities()) {
                manageResources(c);
            }

            timeSinceLastUpdateResources = 0;
            deltaDaysR=0;
        }
        for (City c : p.getPlayerCities()) {
            manageSprite(c, dt, 0);
            manageTerrain(c, dt, 0);
        }
    }

    //TODO revoir completement ce passage (trop gourmand)
    public void manageResources(City c) {
        for (Case r : c.getMap().getSilvers()) {
            ((Rock) r.getTerrain()).setAccessible(false);
            for (Case n : r.getNeighbour()) {
                if (n.getTerrain().isBlocking())
                    continue;
                if (c.getPathManager().getPathTo(c.getMap().getStartCase(), n, FreeState.NON_BLOCKING.getI()) != null) {
                    ((Rock) r.getTerrain()).setAccessible(true);
                    break;
                }
            }
        }
        for (Case r : c.getMap().getCoppers()) {
            ((Rock) r.getTerrain()).setAccessible(false);
            for (Case n : r.getNeighbour()) {
                if (n.getTerrain().isBlocking())
                    continue;
                if (c.getPathManager().getPathTo(c.getMap().getStartCase(), n, FreeState.NON_BLOCKING.getI()) != null) {
                    ((Rock) r.getTerrain()).setAccessible(true);
                    break;
                }
            }
        }
       /*
       for (Case r : c.getMap().getTrees()) {
            ((Tree) r.getObject()).setAccessible(false);
            for (Case n : r.getNeighbour()) {
                if (n == null || n.getTerrain().isBlocking())
                    continue;
                if (c.getPathManager().getPathTo(c.getMap().getStartCase(), n, FreeState.NON_BLOCKING.getI()) != null) {
                    ((Tree) r.getObject()).setAccessible(true);
                    break;
                }
            }
        }
        */
    }


    public void manageTerrain(City c, double deltaTime, int deltaDays) {
        for (Terrain b : c.getTerrain()) {
            b.process(deltaTime, deltaDays);
        }
    }

    public void manageSprite(City c, double deltaTime, int deltaDays) {
        for (Building b : c.getBuildings()) {
            b.processSprite(deltaTime);
        }
        for (Sprite s : c.getResourceManager().getAnimals()) {
            s.process(deltaTime, deltaDays);
        }
    }

    public void manageConstruction(City c, double delaTime, int deltaDays) {
        for (Construction construction : c.getConstructions()) {
            construction.process(delaTime, deltaDays);
        }
    }

    public void manageBuilding(City c, double delaTime, int deltaDays) {
        for (Building b : c.getBuildings()) {
            b.process(delaTime, deltaDays);
            if (b.isActive()) {
                b.populate(delaTime);
            }
        }
        c.deleteQueue();

        City current = p.getPlayerCities().get(currentCity);
        for (Road r : c.getPathManager().getRoads()) {
            r.setActive(current.getPathManager().isReachable(current.getMap().getCase(r.getxPos(), r.getYpos()), current.getMap().getStartCase(), FreeState.ALL_ROAD.i));
        }

        // Plain loop (was a parallelStream): game state is now single-threaded by design.
        for (ObjectClass o : c.getObj()) {
            activate(o);
        }
        c.getBuildingManager().updateWorker();
    }

    public void activate(ObjectClass o) {
        if (o.getBuildingType() == ObjectType.ROAD || o.getAccess() == null)
            return;
        o.setActive(o.getAccess().size() != 0);
    }


}
