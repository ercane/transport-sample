package com.mree.transportsample.model;

import java.util.*;

public class Graph {
    private List<Place> places;
    private List<Path> paths;
    private Map<Place, LinkedHashSet<Place>> map = new HashMap();

    public Graph(List<Place> places, List<Path> paths) {
        this.places = places;
        this.paths = paths;
        generateMap();
    }

    private void generateMap() {
        for (Path p : paths) {
            addEdge(p.getSource(), p.getDestination());
        }
    }

    public void addEdge(Place node1, Place node2) {
        LinkedHashSet<Place> adjacent = map.get(node1);
        if (adjacent == null) {
            adjacent = new LinkedHashSet();
            map.put(node1, adjacent);
        }
        adjacent.add(node2);
    }

    public void addTwoWayVertex(Place node1, Place node2) {
        addEdge(node1, node2);
        addEdge(node2, node1);
    }

    public boolean isConnected(Place node1, Place node2) {
        Set adjacent = map.get(node1);
        if (adjacent == null) {
            return false;
        }
        return adjacent.contains(node2);
    }

    public LinkedList<Place> adjacentNodes(Place last) {
        LinkedHashSet<Place> adjacent = map.get(last);
        if (adjacent == null) {
            return new LinkedList();
        }
        return new LinkedList<Place>(adjacent);
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }
}
