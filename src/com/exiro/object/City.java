package com.exiro.object;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.systemCore.BuildingManager;
import com.exiro.systemCore.PathManager;
import com.exiro.terrainList.Empty;
import com.exiro.terrainList.Terrain;

import java.util.ArrayList;

public class City {

    private String name;
    private Player owner;
    private CityMap map;
    private ArrayList<Construction> constructions;
    private ArrayList<ObjectClass> inActives = new ArrayList<>();
    private ArrayList<ObjectClass> obj = new ArrayList<>();
    //private ArrayList<Sprite> sprites;
    int activeBuilding = 0;

    private ArrayList<Building> buildings;
    private ArrayList<Terrain> terrain;

    private int population, popInArrvial;
    private PathManager pathManager;
    private BuildingManager buildingManager;

    ArrayList<Building> toDestroyB;

    public City(String name, Player owner, CityMap map, ArrayList<Construction> constructions, ArrayList<Building> buildings, int population) {
        this.name = name;
        this.owner = owner;
        this.map = map;
        this.constructions = constructions;
        this.buildings = buildings;
        this.population = population;
    }

    public ArrayList<ObjectClass> getObj() {
        return obj;
    }

    public void setObj(ArrayList<ObjectClass> obj) {
        this.obj = obj;
    }

    public void addObj(ObjectClass o) {
        synchronized (obj) {
            obj.add(o);
        }
    }

    ArrayList<ObjectClass> toDestroyO;

    public void addBuilding(Building o) {
        synchronized (buildings) {
            buildings.add(o);
        }
    }

    ArrayList<ObjectClass> toDestroyC;

    public City(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.buildings = new ArrayList<>();
        this.terrain = new ArrayList<>();
        this.population = 0;
        //this.sprites = new ArrayList<>();
        this.buildingManager = new BuildingManager(this);
        Road start = new Road(this);
        this.constructions = new ArrayList<>();
        Empty e = new Empty(1, this);
        this.map = new CityMap(60, 60, new Case(0, 0, start, e), this);
        this.map.populateMap();
        this.pathManager = new PathManager(owner, this.map);
        start.build(0, 0);
        start.setActive(true);
        start.setStart(true);

        toDestroyB = new ArrayList<>();
        toDestroyC = new ArrayList<>();
        toDestroyO = new ArrayList<>();
    }

    public void removeObj(ObjectClass o) {
        toDestroyO.add(o);
    }

    public void removeBuilding(Building o) {
        toDestroyB.add(o);
    }

    public void deleteQueue() {
        synchronized (buildings) {
            buildings.removeAll(toDestroyB);
            toDestroyB.clear();
        }
        synchronized (obj) {
            obj.removeAll(toDestroyO);
            toDestroyO.clear();
        }
        synchronized (constructions) {
            constructions.removeAll(toDestroyC);
            toDestroyC.clear();
        }
    }

    public void addConstruction(Construction o) {
        synchronized (buildings) {
            constructions.add(o);
        }
    }

    public void removeConstruction(Construction o) {
        toDestroyC.add(o);
    }

    /*
        public ArrayList<Sprite> getSprites() {
            return sprites;
        }

        public void setSprites(ArrayList<Sprite> sprites) {
            this.sprites = sprites;
        }

        public void addSprite(Sprite s) {
            synchronized (sprites) {
                sprites.add(s);
            }
        }

        public void removeSprite(Sprite s) {
            synchronized (sprites) {
                sprites.remove(s);
            }
        }

        public void removeSpriteAll(ArrayList<Sprite> s) {
            synchronized (sprites) {
                sprites.removeAll(s);
            }
        }
    */

    public ArrayList<Terrain> getTerrain() {
        return terrain;
    }

    public void setTerrain(ArrayList<Terrain> terrain) {
        this.terrain = terrain;
    }

    public void addTerrain(Terrain t) {
        this.terrain.add(t);
    }

    public ArrayList<ObjectClass> getInActives() {
        return inActives;
    }

    public void setInActives(ArrayList<ObjectClass> inActives) {
        this.inActives = inActives;
    }

    public int getPopInArrvial() {
        return popInArrvial;
    }

    public void setPopInArrvial(int popInArrvial) {
        this.popInArrvial = popInArrvial;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    public void setBuildingManager(BuildingManager buildingManager) {
        this.buildingManager = buildingManager;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public PathManager getPathManager() {
        return pathManager;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Construction> getConstructions() {
        return constructions;
    }

    public void setConstructions(ArrayList<Construction> constructions) {
        this.constructions = constructions;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public CityMap getMap() {
        synchronized (map) {
            return map;
        }
    }

    public void setMap(CityMap map) {
        this.map = map;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

    public int getActiveBuilding() {
        return activeBuilding;
    }

    public void setActiveBuilding(int activeBuilding) {
        this.activeBuilding = activeBuilding;
    }
}
