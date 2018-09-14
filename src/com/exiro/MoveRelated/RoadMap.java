package com.exiro.MoveRelated;

import com.exiro.BuildingList.BuildingType;
import com.exiro.Object.Case;
import com.exiro.Object.CityMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Creates nodes and neighbours from a 2d grid. Each point in the map has an
 * integer value that specifies the cost of crossing that point. If this value
 * is negative, the point is unreachable.
 * <p>
 * If diagonal movement is allowed, the Chebyshev distance is used, else
 * Manhattan distance is used.
 *
 * @author Ben Ruijl
 */
public class RoadMap {
    private final double[][] map;
    CityMap cityMap;
    private ArrayList<Path> pathsCalculated;

    public RoadMap(CityMap cityMap) {
        this.pathsCalculated = new ArrayList<>();
        this.cityMap = cityMap;
        double[][] map = new double[cityMap.getWidth()][cityMap.getHeight()];
        for (Case c : cityMap.getCases()) {
            if (c.getObject() == null) {
                map[c.getxPos()][c.getyPos()] = 4;
            } else if (c.getBuildingType() == BuildingType.ROAD && c.getObject().isActive()) {
                map[c.getxPos()][c.getyPos()] = 1;
            } else if (c.getBuildingType() == BuildingType.BLOCKABLE_ROAD || (!c.getObject().isActive() && c.getBuildingType() == BuildingType.ROAD)) {
                map[c.getxPos()][c.getyPos()] = 2;
            } else if (c.getBuildingType() == BuildingType.EMPTY) {
                map[c.getxPos()][c.getyPos()] = 3;
            } else {
                map[c.getxPos()][c.getyPos()] = 4;
            }

        }
        this.map = map;
    }

    public ArrayList<Path> getPathsCalculated() {
        return pathsCalculated;
    }

    public void setPathsCalculated(ArrayList<Path> pathsCalculated) {
        this.pathsCalculated = pathsCalculated;
    }

    public Path findPath(int xStart, int yStart, int xGoal, int yGoal, FreeState freeState) {
        List<MapNode> n = PathAlgo.doAStar(new MapNode(xStart, yStart, cityMap.getCase(xStart, yStart).getBuildingType().ordinal()), new MapNode(xGoal, yGoal, cityMap.getCase(xGoal, yGoal).getBuildingType().ordinal()), freeState);
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

    public enum FreeState {
        ONLY_ACTIVE_ROAD(1), ALL_ROAD(2), NON_BLOCKING(3);

        final int i;

        FreeState(int i) {
            this.i = i;
        }
    }

    /**
     * A node in a 2d map. This is simply the coordinates of the point.
     *
     * @author Ben Ruijl
     */
    public class MapNode implements Node<MapNode> {
        private final int x, y, t;

        public MapNode(int x, int y, int t) {
            this.x = x;
            this.y = y;
            this.t = t;
        }

        public int getType() {
            return t;
        }

        public double getHeuristic(MapNode goal) {
            return Math.abs(x - goal.x) + Math.abs(y - goal.y);
        }

        public double getTraversalCost(MapNode neighbour) {
            return 1 + map[neighbour.y][neighbour.x];
        }

        public Set<MapNode> getNeighbours() {
            Set<MapNode> neighbours = new HashSet<MapNode>();

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if ((i == x && j == y) || i < 0 || j < 0 || j >= map.length
                            || i >= map[j].length) {
                        continue;
                    }

                    if ((i < x && j < y) ||
                            (i < x && j > y) ||
                            (i > x && j > y) ||
                            (i > x && j < y)) {
                        continue;
                    }

                    if (map[i][j] < 0) {
                        continue;
                    }


                    // TODO: create cache instead of recreation
                    neighbours.add(new MapNode(i, j, (int) map[i][j]));
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
