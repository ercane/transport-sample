package com.mree.transportsample;

import com.mree.transportsample.model.Graph;
import com.mree.transportsample.model.Path;
import com.mree.transportsample.model.Place;

import java.io.*;
import java.util.*;

public class Main {

    private static List<Place> PLACES = new ArrayList<Place>();
    private static List<String[][]> LINK_WEIGHT_MATRIXES = new ArrayList<String[][]>();

    public static void main(String[] args) {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource("places.csv").getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            int counter = 0;
            while (line != null) {
                PLACES.add(getPlaceFromLine(counter, line));
                counter++;
                line = reader.readLine();
            }
            reader.close();

            Graph graph = generateGraph();

            generateLinkWeightMatrixes(graph);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Graph generateGraph() {
        List<Path> allPathList = new ArrayList<Path>();
        for (Place place : PLACES) {
            for (Integer id : place.getPaths()) {
                Path p = new Path();
                p.setSource(place);
                p.setDestination(PLACES.get(id));
                allPathList.add(p);
            }
        }

        Graph graph = new Graph(PLACES, allPathList);
        return graph;
    }

    private static void generateLinkWeightMatrixes(Graph graph) {
        for (Path path : graph.getPaths()) {
            String[][] matrix = new String[PLACES.size()][PLACES.size()];
            for (int i = 0; i < PLACES.size(); i++) {
                for (int j = 0; j < PLACES.size(); j++) {
                    Place source = PLACES.get(i);
                    Place target = PLACES.get(j);

                    if (source == target) {
                        matrix[i][j] = "0";
                    } else {
                        List<LinkedList<Place>> pathList = new ArrayList<LinkedList<Place>>();
                        List<LinkedList<Place>> filteredPathList = new ArrayList<LinkedList<Place>>();
                        LinkedList<Place> visited = new LinkedList();
                        visited.add(source);
                        depthFirst(graph, visited, target, pathList);

                        Collections.sort(pathList, new Comparator<LinkedList<Place>>() {
                            public int compare(LinkedList<Place> o1, LinkedList<Place> o2) {
                                Integer size1 = o1.size();
                                Integer size2 = o2.size();
                                return size1.compareTo(size2);
                            }
                        });

                        if (!pathList.isEmpty()) {
                            int temp = pathList.get(0).size();
                            filteredPathList.add(pathList.get(0));
                            for (int k = 1; k < pathList.size(); k++) {
                                if (temp == pathList.get(k).size()) {
                                    filteredPathList.add(pathList.get(k));
                                } else {
                                    break;
                                }
                            }
                        }

                        List<LinkedList<Place>> foundList = new ArrayList<LinkedList<Place>>();
                        for (LinkedList<Place> p : filteredPathList) {
                            for (int k = 0; k < p.size() - 1; k++) {
                                if (p.get(k).equals(path.getSource())
                                        && p.get(k + 1).equals(path.getDestination())) {
                                    foundList.add(p);
                                    break;
                                }
                            }
                        }

                        Collections.sort(foundList, new Comparator<LinkedList<Place>>() {
                            public int compare(LinkedList<Place> o1, LinkedList<Place> o2) {
                                Integer size1 = o1.size();
                                Integer size2 = o2.size();
                                return size1.compareTo(size2);
                            }
                        });

                        if (!foundList.isEmpty()) {
                            int count = 1;
                            int temp = foundList.get(0).size();
                            for (int k = 1; k < foundList.size(); k++) {
                                if (temp == foundList.get(k).size()) {
                                    count++;
                                } else {
                                    break;
                                }
                            }
                            matrix[i][j] = 1/count + "";
                        } else {
                            matrix[i][j] = "0";
                        }

                    }


                }
            }
            LINK_WEIGHT_MATRIXES.add(matrix);
            System.out.println("Path: " + path.getSource() + "-" + path.getDestination());
            printMatrix(matrix);
        }
    }

    private static void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void depthFirst(Graph graph, LinkedList<Place> visited, Place end, List<LinkedList<Place>> pathList) {
        LinkedList<Place> nodes = graph.adjacentNodes(visited.getLast());
        // examine adjacent nodes
        for (Place node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(end)) {
                visited.add(node);
                //printPath(visited);
                pathList.add(new LinkedList<Place>(visited));
                visited.removeLast();
                break;
            }
        }
        for (Place node : nodes) {
            if (visited.contains(node) || node.equals(end)) {
                continue;
            }
            visited.addLast(node);
            depthFirst(graph, visited, end, pathList);
            visited.removeLast();
        }
    }

    private static void printPath(LinkedList<Place> visited) {
        for (Place node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
    }

    private static Place getPlaceFromLine(int counter, String line) {
        String[] split = line.split(";");

        Place p = new Place();
        p.setId(counter);
        p.setName(split[0]);

        String[] paths = split[1].split(",");
        List<Integer> pathList = new ArrayList<Integer>();
        for (String s : paths) {
            pathList.add(Integer.parseInt(s));
        }
        p.setPaths(pathList);
        return p;
    }
}
