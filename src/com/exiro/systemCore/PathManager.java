package com.exiro.systemCore;

import com.exiro.constructionList.Road;
import com.exiro.moveRelated.Path;
import com.exiro.moveRelated.RoadMap;
import com.exiro.object.Case;
import com.exiro.object.CityMap;
import com.exiro.object.Player;

import java.util.ArrayList;

public class PathManager {

    private Player p;
    private ArrayList<Road> roads;
    private ArrayList<Road> lastroads;
    private ArrayList<Path> paths;
    private CityMap map;
    private RoadMap roadMap;

    public PathManager(Player p, ArrayList<Road> roads, ArrayList<Path> paths, CityMap map) {
        this.p = p;
        this.roads = roads;
        this.lastroads = new ArrayList<>(roads);
        this.paths = paths;
        this.map = map;
        this.roadMap = new RoadMap(map);
    }

    public PathManager(Player p, CityMap map) {
        this.p = p;
        this.map = map;
        this.roads = new ArrayList<>();
        this.lastroads = new ArrayList<>(roads);
        this.paths = new ArrayList<>();
        this.roadMap = new RoadMap(map);
    }

    public void addRoad(Road r) {
        synchronized (roads) {
            roads.add(r);
            for (Road road : roads) {
                road.updateRoadImg();
            }
        }
    }

    public void deleteRoad(Road r) {
        synchronized (roads) {
            roads.remove(r);
            for (Road road : roads) {
                road.updateRoadImg();
            }
        }
    }

    public boolean isInContact(Case c1, Case c2) {
        return (c1.getxPos() - c2.getxPos()) * (c1.getxPos() - c2.getxPos()) + (c1.getyPos() - c2.getyPos()) * (c1.getyPos() - c2.getyPos()) <= 2;
    }

    public Path getPathTo(Case c1, Case c2, int free) {
        // if(map.getCase(c1.getxPos(),c1.getyPos())==null || map.getCase(c2.getxPos(),c2.getyPos())==null)
        //   return null;
        if (!lastroads.equals(roads)) {
            roadMap = new RoadMap(map);
            this.lastroads = new ArrayList<>(roads);
        }
        if (c1 == null || c2 == null)
            return null;
        return getPath(c1, c2, free) == null ? roadMap.findPath(c1.getxPos(), c1.getyPos(), c2.getxPos(), c2.getyPos(), free) : getPath(c1, c2, free);//verifie si il existe un chemin deja calculé
    }

    public Path getPathTo(int x1, int y1, int x2, int y2, int free) {
        // if(map.getCase(c1.getxPos(),c1.getyPos())==null || map.getCase(c2.getxPos(),c2.getyPos())==null)
        //   return null;
        if (!lastroads.equals(roads)) {
            roadMap = new RoadMap(map);
            this.lastroads = new ArrayList<>(roads);
        }
        return roadMap.findPath(x1, y1, x2, y2, free);//verifie si il existe un chemin deja calculé
    }

    public boolean isReachable(Case c1, Case c2, int free) {
        if (!lastroads.equals(roads)) {
            roadMap = new RoadMap(map);
            this.lastroads = new ArrayList<>(roads);
        }
        return getPathTo(c1, c2, free) != null;
    }


    public Path getPath(Case c1, Case c2, int free) {
        /*for(Path p : roadMap.getPathsCalculated()){
            if(p.getPath().contains(c1) && p.getPath().contains(c2) && p.isFree()==free) {
                Path p2 = new Path(p.getPath());
              //  p2.setStart(p.getPath().get(0));
             //   p2.setEnd(p.getPath().get(p.getPath().size()-1));
                p2.setFree(free);
                return p2;
            }
        }*/
        return null;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public CityMap getMap() {
        return map;
    }

    public void setMap(CityMap map) {
        this.map = map;
    }
}
