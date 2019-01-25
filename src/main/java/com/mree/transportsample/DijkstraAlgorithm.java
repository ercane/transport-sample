package com.mree.transportsample;

import com.mree.transportsample.model.Graph;
import com.mree.transportsample.model.Path;
import com.mree.transportsample.model.Place;

import java.util.*;

public class DijkstraAlgorithm {
    private final List<Place> nodes;
    private final List<Path> Paths;
    private Set<Place> settledNodes;
    private Set<Place> unSettledNodes;
    private Map<Place, Place> predecessors;
    private Map<Place, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Place>(graph.getPlaces());
        this.Paths = new ArrayList<Path>(graph.getPaths());
    }

    public void execute(Place source) {
        settledNodes = new HashSet<Place>();
        unSettledNodes = new HashSet<Place>();
        distance = new HashMap<Place, Integer>();
        predecessors = new HashMap<Place, Place>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Place node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Place node) {
        List<Place> adjacentNodes = getNeighbors(node);
        for (Place target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Place node, Place target) {
        for (Path Path : Paths) {
            if (Path.getSource().equals(node)
                    && Path.getDestination().equals(target)) {
                return Path.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Place> getNeighbors(Place node) {
        List<Place> neighbors = new ArrayList<Place>();
        for (Path Path : Paths) {
            if (Path.getSource().equals(node)
                    && !isSettled(Path.getDestination())) {
                neighbors.add(Path.getDestination());
            }
        }
        return neighbors;
    }

    private Place getMinimum(Set<Place> Placees) {
        Place minimum = null;
        for (Place Place : Placees) {
            if (minimum == null) {
                minimum = Place;
            } else {
                if (getShortestDistance(Place) < getShortestDistance(minimum)) {
                    minimum = Place;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Place Place) {
        return settledNodes.contains(Place);
    }

    private int getShortestDistance(Place destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Place> getPath(Place target) {
        LinkedList<Place> path = new LinkedList<Place>();
        Place step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }


}
