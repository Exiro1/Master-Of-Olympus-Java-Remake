package com.exiro.systemCore;

import com.exiro.constructionList.Road;
import com.exiro.moveRelated.Path;
import com.exiro.moveRelated.RoadMap;
import com.exiro.object.Case;
import com.exiro.object.CityMap;
import com.exiro.object.Player;
import com.exiro.terrainList.Water;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PathManager {

    private Player p;
    private ArrayList<Road> roads;
    private ArrayList<Path> paths;
    private CityMap map;
    // RoadMap keeps no state about the roads, so one instance is enough
    private final RoadMap roadMap;

    public PathManager(Player p, ArrayList<Road> roads, ArrayList<Path> paths, CityMap map) {
        this.p = p;
        this.roads = roads;
        this.paths = paths;
        this.map = map;
        this.roadMap = new RoadMap(map);
    }

    public PathManager(Player p, CityMap map) {
        this.p = p;
        this.map = map;
        this.roads = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.roadMap = new RoadMap(map);
    }

    public void addRoad(Road r) {
        roads.add(r);
        updateRoadImgAround(r);
    }

    public void deleteRoad(Road r) {
        roads.remove(r);
        updateRoadImgAround(r);
    }

    /**
     * Only the road and its 4 neighbours can change shape, no need to redo every road of the map.
     */
    private void updateRoadImgAround(Road r) {
        Case c = r.getMainCase();
        if (c == null)
            return;
        if (c.getObject() == r)
            r.updateRoadImg();
        for (Case n : c.getNeighbour()) {
            if (n != null && n.getObject() instanceof Road)
                ((Road) n.getObject()).updateRoadImg();
        }
    }

    public boolean isInContact(Case c1, Case c2) {
        return (c1.getxPos() - c2.getxPos()) * (c1.getxPos() - c2.getxPos()) + (c1.getyPos() - c2.getyPos()) * (c1.getyPos() - c2.getyPos()) <= 2;
    }

    public Path getPathTo(Case c1, Case c2, int free, boolean straight) {
        if (c1 == null || c2 == null)
            return null;
        return roadMap.findPath(c1.getxPos(), c1.getyPos(), c2.getxPos(), c2.getyPos(), free, straight);
    }

    public Path getPathTo(Case c1, Case c2, int free) {
        if (c1 == null || c2 == null)
            return null;
        return roadMap.findPath(c1.getxPos(), c1.getyPos(), c2.getxPos(), c2.getyPos(), free);
    }

    public Path getPathTo(int x1, int y1, int x2, int y2, int free) {
        return roadMap.findPath(x1, y1, x2, y2, free);
    }

    public boolean isReachable(Case c1, Case c2, int free) {
        return getPathTo(c1, c2, free) != null;
    }

    /**
     * Every case reachable from {@code start} with straight moves, walking only on cases whose
     * free state matches {@code free} (same rules as the A* in {@link RoadMap}). One flood fill
     * answers "is X reachable from start" for all X at once, instead of one A* per X.
     */
    public Set<Case> reachableFrom(Case start, int free) {
        Set<Case> visited = new HashSet<>();
        if (start == null)
            return visited;
        ArrayDeque<Case> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            Case c = queue.poll();
            for (Case n : c.getNeighbour()) {
                if (n == null || visited.contains(n))
                    continue;
                if (n.getTerrain().isBlocking() && !(n.getTerrain() instanceof Water))
                    continue;
                if ((RoadMap.getFreeState(n.getxPos(), n.getyPos(), map).getI() & free) == 0)
                    continue;
                visited.add(n);
                queue.add(n);
            }
        }
        return visited;
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
