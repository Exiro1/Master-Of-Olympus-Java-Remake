package com.exiro.moveRelated;

import java.util.*;

public class PathAlgo {


    public static <T extends Node<T>> List<T> doAStar(T start, T goal, int free) {
        Set<T> closed = new HashSet<>();
        Map<T, T> fromMap = new HashMap<>();
        List<T> route = new LinkedList<>();
        Map<T, Double> gScore = new HashMap<>();
        final Map<T, Double> fScore = new HashMap<>();
        PriorityQueue<T> open = new PriorityQueue<>(11, (nodeA, nodeB) -> Double.compare(fScore.get(nodeA), fScore.get(nodeB)));

        gScore.put(start, 0.0);
        fScore.put(start, start.getHeuristic(goal));
        open.offer(start);
        while (!open.isEmpty()) {
            T current = open.poll();
            if (current.equals(goal)) {
                while (current != null) {
                    route.add(0, current);
                    current = fromMap.get(current);
                }

                return route;
            }

            closed.add(current);

            for (T neighbour : current.getNeighbours()) {
                if (closed.contains(neighbour)) {
                    continue;
                }

                double tentG = gScore.get(current)
                        + current.getTraversalCost(neighbour, fromMap.get(current));

                boolean contains = open.contains(neighbour);
                if ((!contains || tentG < gScore.get(neighbour)) && (((neighbour.getType().getI() & free) != 0))) {
                    gScore.put(neighbour, tentG);
                    fScore.put(neighbour, tentG + neighbour.getHeuristic(goal));

                    if (contains) {
                        open.remove(neighbour);
                    }

                    open.offer(neighbour);
                    fromMap.put(neighbour, current);
                }
            }
        }

        return null;
    }

}