package com.exiro.object;

import com.exiro.buildingList.Building;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.fileManager.MapSettings;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.systemCore.BuildingManager;
import com.exiro.systemCore.PathManager;
import com.exiro.terrainList.Terrain;

import java.util.ArrayList;
import java.util.HashMap;

public class City {

    private String name;
    private Player owner;
    private CityMap map;
    private ArrayList<Construction> constructions;
    private ArrayList<ObjectClass> inActives = new ArrayList<>();
    private ArrayList<ObjectClass> obj = new ArrayList<>();

    private ResourceManager resourceManager;


    private HashMap<ObjectType, ArrayList<Building>> buildingMap;

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
        this.buildingMap = new HashMap<>();
        resourceManager = new ResourceManager();
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
        if(!buildingMap.containsKey(o.getBuildingType()))
            buildingMap.put(o.getBuildingType(),new ArrayList<>());
        buildingMap.get(o.getBuildingType()).add(o);
    }

    ArrayList<ObjectClass> toDestroyC;

    public City(String name, Player owner) {
        toDestroyB = new ArrayList<>();
        toDestroyC = new ArrayList<>();
        toDestroyO = new ArrayList<>();
        resourceManager = new ResourceManager();
        this.buildingMap = new HashMap<>();
        this.name = name;
        this.owner = owner;
        this.buildings = new ArrayList<>();
        this.terrain = new ArrayList<>();
        this.population = 0;
        //this.sprites = new ArrayList<>();
        this.buildingManager = new BuildingManager(this);
        Road start = new Road(this);
        this.constructions = new ArrayList<>();

        //TODO voir comment faire le systeme de chargement de map proprement
        this.map = new CityMap(MapSettings.loadSettings("Assets/savedMap.map"), this);

        this.map.populateMap();
        this.pathManager = new PathManager(owner, this.map);
        start.build(map.getStartCase().getxPos(), map.getStartCase().getyPos());
        start.setActive(true);
        start.setStart(true);


        generateStartRoads();



    }

    public void generateStartRoads(){
        Path p = pathManager.getPathTo(map.getStartCase(),map.getEndCase(), FreeState.NON_BLOCKING.getI());
        if(p == null){
           return;
        }
        int index = 0;
        for(Case c : p.getPath()){
                Road r = new Road(this);
                c.getTerrain().setConstructible(true);
                if(c.getObject() != null)
                    c.getObject().delete();
                c.setObject(null);
                r.build(c.getxPos(),c.getyPos());
                if(index < 8)
                    r.setInDestructible(true);
                    c.getTerrain().setConstructible(false);
                index++;
        }
    }

    public void removeObj(ObjectClass o) {
        toDestroyO.add(o);
    }

    public void removeBuilding(Building o) {
        toDestroyB.add(o);
        buildingMap.get(o.getBuildingType()).remove(o);
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

    public ArrayList<Building> getBuildingList(ObjectType buildingType) {
        return buildingMap.getOrDefault(buildingType, new ArrayList<>());
    }


    public ResourceManager getResourceManager() {
        return resourceManager;
    }


    public void removeConstruction(Construction o) {
        toDestroyC.add(o);
    }

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
        if (map == null)
            return null;
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
