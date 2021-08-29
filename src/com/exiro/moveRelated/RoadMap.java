package com.exiro.moveRelated;

import com.exiro.object.CityMap;
import com.exiro.object.ObjectType;
import com.exiro.terrainList.Elevation;
import com.exiro.terrainList.Meadow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoadMap {
    //private final double[][] map;

    CityMap cityMap;
    private ArrayList<Path> pathsCalculated;

    public RoadMap(CityMap cityMap) {
        this.pathsCalculated = new ArrayList<>();
        this.cityMap = cityMap;
        /*
        double[][] map = new double[cityMap.getWidth()][cityMap.getHeight()];
        for (Case c : cityMap.getCases()) {
            if (c.getObject() == null) {
                map[c.getxPos()][c.getyPos()] = 4;
            } else if (c.getBuildingType() == ObjectType.ROAD && c.getObject().isActive()) {
                map[c.getxPos()][c.getyPos()] = 1;
            } else if (c.getBuildingType() == ObjectType.BLOCKABLE_ROAD || (!c.getObject().isActive() && c.getBuildingType() == ObjectType.ROAD)) {
                map[c.getxPos()][c.getyPos()] = 2;
            } else if (c.getBuildingType() == ObjectType.EMPTY) {
                map[c.getxPos()][c.getyPos()] = 3;
            } else {
                map[c.getxPos()][c.getyPos()] = 4;
            }
        }
        this.map = map;
        */

    }

    public ArrayList<Path> getPathsCalculated() {
        return pathsCalculated;
    }

    public void setPathsCalculated(ArrayList<Path> pathsCalculated) {
        this.pathsCalculated = pathsCalculated;
    }

    public Path findPath(int xStart, int yStart, int xGoal, int yGoal, int freeState) {
        FreeState accessa = getFreeState(xStart, yStart);

        FreeState accessb = getFreeState(xGoal, yGoal);

        List<MapNode> n = PathAlgo.doAStar(new MapNode(xStart, yStart, accessa), new MapNode(xGoal, yGoal, accessb), freeState);
        if (n == null)
            return null;
        Path p = new Path(new ArrayList<>());
        p.setStart(cityMap.getCase(xStart, yStart));
        p.setEnd(cityMap.getCase(xGoal, yGoal));
        p.setFree(freeState);
        for (MapNode mn : n) {
            p.addCases(cityMap.getCase(mn.x, mn.y));
        }
        Path newpath = new Path(p.getPath());
        newpath.setStart(cityMap.getCase(xStart, yStart));
        newpath.setEnd(cityMap.getCase(xGoal, yGoal));
        newpath.setFree(freeState);
        //pathsCalculated.add(newpath);
        return p;
    }

    public static FreeState getFreeState(int xStart, int yStart, CityMap cityMap) {
        FreeState access = FreeState.BLOCKED;
        if (!cityMap.getCase(xStart, yStart).getTerrain().isBlocking()) {
            if (cityMap.getCase(xStart, yStart).getObject() == null) {
                if (cityMap.getCase(xStart, yStart).getTerrain().isConstructible()) {
                    access = FreeState.BUILDABLE;
                    if (cityMap.getCase(xStart, yStart).getTerrain() instanceof Elevation)
                        access = FreeState.BUILDABLE_ROAD;
                    if (cityMap.getCase(xStart, yStart).getTerrain() instanceof Meadow)
                        access = FreeState.MEADOW;
                } else {
                    access = FreeState.NON_BLOCKING;
                }
            } else if (cityMap.getCase(xStart, yStart).getObject().getBuildingType() == ObjectType.ROAD && cityMap.getCase(xStart, yStart).getObject().isActive()) {
                access = FreeState.ONLY_ACTIVE_ROAD;
            } else if (cityMap.getCase(xStart, yStart).getObject().getBuildingType() == ObjectType.BLOCKABLE_ROAD || (!cityMap.getCase(xStart, yStart).getObject().isActive() && cityMap.getCase(xStart, yStart).getObject().getBuildingType() == ObjectType.ROAD)) {
                access = FreeState.BLOCKING_ROAD;
            } else if (cityMap.getCase(xStart, yStart).getObject().getBuildingType() == ObjectType.TREE) {
                access = FreeState.WALKABLE;
            }
        }
        return access;
    }

    public FreeState getFreeState(int xStart, int yStart) {
        return getFreeState(xStart, yStart, cityMap);
    }


    /**
     * A node in a 2d map. This is simply the coordinates of the point.
     *
     * @author Ben Ruijl
     */
    public class MapNode implements Node<MapNode> {
        private final int x, y;
        FreeState t;

        public MapNode(int x, int y, FreeState t) {
            this.x = x;
            this.y = y;
            this.t = t;
        }

        public FreeState getType() {
            return t;
        }

        public double getHeuristic(MapNode goal) {
            return Math.abs(x - goal.x) + Math.abs(y - goal.y);
        }

        public double getTraversalCost(MapNode neighbour, MapNode last) {
            //FreeState access = 0;
            int i = neighbour.x;
            int j = neighbour.y;

            if (last == null || last == neighbour)
                return 1;

            int k = last.x;
            int l = last.y;

            if (i == k || j == l)
                return 1;

            //access = getFreeState(i,j);
            return 5;
        }

        public Set<MapNode> getNeighbours() {
            Set<MapNode> neighbours = new HashSet<>();

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if ((i == x && j == y) || i < 0 || j < 0 || j >= cityMap.getWidth()
                            || i >= cityMap.getHeight()) {
                        continue;
                    }

                    if ((i < x && j < y) ||
                            (i < x && j > y) ||
                            (i > x && j > y) ||
                            (i > x && j < y)) {
                        continue;
                    }

                    if (cityMap.getCase(i, j).getTerrain().isBlocking()) {
                        continue;
                    }

                    neighbours.add(new MapNode(i, j, getFreeState(i, j)));
                }
            }

            return neighbours;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MapNode other = (MapNode) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (x != other.x)
                return false;
            return y == other.y;
        }

        private RoadMap getOuterType() {
            return RoadMap.this;
        }

    }
}
