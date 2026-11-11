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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    // every city of every player is simulated, not only the one on screen
    private final World world;
    private final GameManager gm;
    private final SoundManager sm;

    Random ran;
    public Simulation(GameManager gm, SoundManager sm) {
        this.world = gm.getWorld();
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
        sm.updateMusic();

        int deltaDays = world.getTimeManager().updateTime(dt);
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
            if(sm.secBeforeNextLayer1 == 0)
                sm.playNewLayer1();
            if(sm.secBeforeNextLayer1 >= 0)
                sm.secBeforeNextLayer1--;

            for (City c : world.getCities()) {
                manageBuilding(c, timeSinceLastUpdateBuilding,deltaDaysB);
            }
            timeSinceLastUpdateBuilding = 0;
            deltaDaysB=0;
        }
        if (timeSinceLastUpdateConstruct > 3) {

            for (City c : world.getCities()) {
                manageConstruction(c, timeSinceLastUpdateConstruct, deltaDaysC);
                c.getResourceManager().process();
            }

            timeSinceLastUpdateConstruct = 0;
            deltaDaysC=0;
        }
        if (timeSinceLastUpdateResources > 30) {

            for (City c : world.getCities()) {
                manageResources(c);
            }

            timeSinceLastUpdateResources = 0;
            deltaDaysR=0;
        }
        for (City c : world.getCities()) {
            manageSprite(c, dt, 0);
            manageTerrain(c, dt, 0);
        }
    }

    public void manageResources(City c) {
        // one flood fill from the start case instead of one A* per ore neighbour
        Set<Case> reachable = c.getPathManager().reachableFrom(c.getMap().getStartCase(), FreeState.NON_BLOCKING.getI());
        updateRockAccess(c.getMap().getSilvers(), reachable);
        updateRockAccess(c.getMap().getCoppers(), reachable);
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

    private void updateRockAccess(List<Case> rocks, Set<Case> reachable) {
        for (Case r : rocks) {
            boolean accessible = false;
            for (Case n : r.getNeighbour()) {
                if (n != null && !n.getTerrain().isBlocking() && reachable.contains(n)) {
                    accessible = true;
                    break;
                }
            }
            ((Rock) r.getTerrain()).setAccessible(accessible);
        }
    }


    public void manageTerrain(City c, double deltaTime, int deltaDays) {
        for (Terrain b : c.getTerrain()) {
            b.process(deltaTime, deltaDays);
        }
    }

    public void manageSprite(City c, double deltaTime, int deltaDays) {
        for (Building b : c.getBuildings()) {
            b.stopIfIdle();
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

        // A road is active when it is connected to the start case: one flood fill for all of them
        Case startCase = c.getMap().getStartCase();
        Set<Case> connected = c.getPathManager().reachableFrom(startCase, FreeState.ALL_ROAD.i);
        for (Road r : c.getPathManager().getRoads()) {
            r.setActive(connected.contains(c.getMap().getCase(r.getxPos(), r.getYpos())));
        }

        // Plain loop (was a parallelStream): game state is now single-threaded by design.
        for (ObjectClass o : c.getObj()) {
            activate(o);
        }
        c.getBuildingManager().updateWorker();
    }

    public void activate(ObjectClass o) {
        if (o.getBuildingType() == ObjectType.ROAD)
            return;
        ArrayList<Case> access = o.getAccess();
        if (access != null)
            o.setActive(!access.isEmpty());
    }


}
