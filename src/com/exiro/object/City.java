package com.exiro.object;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.buildingList.culture.Podium;
import com.exiro.buildingList.culture.Theater;
import com.exiro.constructionList.Construction;
import com.exiro.constructionList.Road;
import com.exiro.fileManager.MapSettings;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.animals.Goat;
import com.exiro.sprite.animals.Sheep;
import com.exiro.systemCore.BuildingManager;
import com.exiro.systemCore.PathManager;
import com.exiro.terrainList.Terrain;

import java.util.ArrayList;

public class City {

    private String name;
    private Player owner;
    private CityMap map;
    private ArrayList<Construction> constructions;
    private ArrayList<ObjectClass> inActives = new ArrayList<>();
    private ArrayList<ObjectClass> obj = new ArrayList<>();

    private final ArrayList<Sprite> animals = new ArrayList<>();
    private final ArrayList<Sheep> sheeps = new ArrayList<>();
    private final ArrayList<Goat> goats = new ArrayList<>();

    private final ArrayList<StoreBuilding> storage;
    private final ArrayList<Theater> theaters;
    private final ArrayList<Podium> podiums;

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
        storage = new ArrayList<>();
        theaters = new ArrayList<>();
        podiums = new ArrayList<>();
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
        //TODO voir comment faire le systeme de chargement de map proprement
        this.map = new CityMap(MapSettings.loadSettings("Assets/savedMap.map"), this);

        this.map.populateMap();
        this.pathManager = new PathManager(owner, this.map);
        start.build(map.getStartCase().getxPos(), map.getStartCase().getyPos());
        start.setActive(true);
        start.setStart(true);

        generateStartRoads();

        toDestroyB = new ArrayList<>();
        toDestroyC = new ArrayList<>();
        toDestroyO = new ArrayList<>();

        storage = new ArrayList<>();
        theaters = new ArrayList<>();
        podiums = new ArrayList<>();
    }

    public void generateStartRoads(){
        Path p = pathManager.getPathTo(map.getStartCase(),map.getCase(map.getStartCase().getxPos(),map.getStartCase().getyPos()+15), FreeState.BUILDABLE.getI());
        if(p == null){
            p = pathManager.getPathTo(map.getStartCase(),map.getCase(map.getStartCase().getxPos(),map.getStartCase().getyPos()-15), FreeState.BUILDABLE.getI());
        }
        if(p == null){
            p = pathManager.getPathTo(map.getStartCase(),map.getCase(map.getStartCase().getxPos()+15,map.getStartCase().getyPos()), FreeState.BUILDABLE.getI());
        }
        if(p == null){
            p = pathManager.getPathTo(map.getStartCase(),map.getCase(map.getStartCase().getxPos()-15,map.getStartCase().getyPos()), FreeState.BUILDABLE.getI());
        }
        if(p == null){
           return;
        }
        for(Case c : p.getPath()){
            if(!c.isOccupied()){
                Road r = new Road(this);
                c.getTerrain().setConstructible(true);
                r.build(c.getxPos(),c.getyPos());
                c.getTerrain().setConstructible(false);
            }
        }
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


    public void addStorage(StoreBuilding o) {
        synchronized (storage) {
            storage.add(o);
        }
    }

    public void addTheater(Theater o) {
        synchronized (theaters) {
            theaters.add(o);
        }
    }

    public void addPodium(Podium o) {
        synchronized (podiums) {
            podiums.add(o);
        }
    }

    public void removeStorage(StoreBuilding o) {
        synchronized (storage) {
            storage.remove(o);
        }
    }

    public void removeTheater(Theater o) {
        synchronized (theaters) {
            theaters.remove(o);
        }
    }

    public void removePodium(Podium o) {
        synchronized (podiums) {
            podiums.remove(o);
        }
    }

    public void removeAnimal(Sprite o) {
        synchronized (animals) {
            animals.remove(o);
        }
    }

    public void addAnimal(Sprite s) {
        synchronized (animals) {
            animals.add(s);
        }
    }

    public void addSheep(Sheep s) {
        addAnimal(s);
        synchronized (sheeps) {
            sheeps.add(s);
        }
    }

    public void removeSheep(Sheep s) {
        removeAnimal(s);
        synchronized (sheeps) {
            sheeps.remove(s);
        }
    }

    public ArrayList<Sheep> getSheeps() {
        return sheeps;
    }

    public void addGoat(Goat s) {
        addAnimal(s);
        synchronized (goats) {
            goats.add(s);
        }
    }

    public void removeGoat(Goat s) {
        removeAnimal(s);
        synchronized (goats) {
            goats.remove(s);
        }
    }

    public ArrayList<Goat> getGoats() {
        return goats;
    }

    public ArrayList<Sprite> getAnimals() {
        return animals;
    }

    public ArrayList<StoreBuilding> getStorage() {
        return storage;
    }

    public ArrayList<Theater> getTheaters() {
        return theaters;
    }

    public ArrayList<Podium> getPodiums() {
        return podiums;
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
