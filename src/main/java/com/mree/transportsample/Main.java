package com.mree.transportsample;

import com.mree.transportsample.model.Graph;
import com.mree.transportsample.model.Path;
import com.mree.transportsample.model.Place;
import com.mree.transportsample.util.PrintUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static com.mree.transportsample.util.PrintUtils.printMatrix;

public class Main {

    private static List<Place> PLACES = new ArrayList<Place>();
    private static List<String[][]> LINK_WEIGHT_MATRIXES = new ArrayList<String[][]>();
    private static Map<Integer, Long> populationMap = new TreeMap<Integer, Long>();
    private static Map<String, Double> distanceMap = new TreeMap<String, Double>();
    private static Map<String, Integer> tripMap = new TreeMap<String, Integer>();
    private static Map<String, String[][]> linkWeightMap = new TreeMap<String, String[][]>();
    private static Map<String, Double> flowObservedMap = new TreeMap<String, Double>();
    private static Map<String, Double> flowGuessedMap = new TreeMap<String, Double>();
    private static String[][] baseBsMatrix;

    public static void main(String[] args) {
        try {
            readPlaces();
            readPopulation();
            readDistance();

            generateBaseBasMatrix();
            Graph graph = generateGraph();

            generateLinkWeightMatrixes(graph);
            PrintUtils.printLinked(linkWeightMap);
            generateTripMap(graph);
            PrintUtils.printTrip(tripMap);
            generateFlowObservedMap(graph);
            PrintUtils.printFlow(flowObservedMap);
            generateGuessedFlowMap(graph);
            PrintUtils.printFlow(flowGuessedMap);

            generateAbsoluteMistake();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateBaseBasMatrix() {
        int size = PLACES.size();
        baseBsMatrix = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    baseBsMatrix[i][j] = "0.0";
                else
                    baseBsMatrix[i][j] = "1.0";
            }
        }


    }

    private static void generateTripMap(Graph graph) {
        for (Path path : graph.getPaths()) {
            String key = path.getSource().getId() + "" + path.getDestination().getId();
            if (path.getSource().getId() > path.getDestination().getId()) {
                key = path.getDestination().getId() + "" + path.getSource().getId();
            }
            Integer value = tripMap.get(key);
            if (value == null)
                value = 1;
            else
                value++;

            tripMap.put(key, value);
        }
    }

    private static void generateFlowObservedMap(Graph graph) {

    }

    private static void generateGuessedFlowMap(Graph graph) {
        for (Path path : graph.getPaths()) {
            Place source = path.getSource();
            Place dest = path.getDestination();
            String key = source.getId() + "" + dest.getId();
            if (source.getId() > dest.getId())
                continue;
            String[][] matrix = linkWeightMap.get(key);
            int i = 0, j = 1;
            Double result = 0.0;
            for (i = 0; i < PLACES.size(); i++) {
                for (j = 0; i < PLACES.size(); i++) {
                    String innerKey = i + "" + j;
                    if (i > j)
                        innerKey = j + "" + i;
                    Integer trip = tripMap.get(innerKey);
                    if (trip == null) {
                        trip = 0;
                    }
                    result += trip * Double.parseDouble(matrix[i][j]);
                }
            }
            flowGuessedMap.put(key, result);
        }
    }

    private static void generateAbsoluteMistake() {

        for (int i = 0; i <= 20; i++) {
            for (int j = 0; j <= 20; j++) {
                generateMattop(i, j);
            }
        }

    }

    private static void generateMattop(int x, int y) {
        Double mattop = 0.0;
        BigDecimal mt = new BigDecimal("0.0");
        int i = 0, j = 1;
        for (i = 0; i < linkWeightMap.keySet().size() - 1; i++) {
            for (j = 1; j < linkWeightMap.keySet().size(); j++) {
                Long iPop = populationMap.get(i);
                Long jPop = populationMap.get(j);
                BigDecimal popBd = new BigDecimal(iPop * jPop);
                Double distance = distanceMap.get(i + "" + j);
                BigDecimal distBd = new BigDecimal(distance);
                try {
                    popBd.pow(x);
                    distBd.pow(y);
                    //mattop += Math.pow(iPop * jPop, 3) / Math.pow(distance, 8);
                    BigDecimal divide = popBd.divide(distBd);
                    mt = mt.add(divide);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        System.out.println("Mattop: " + mt);

    }

    private static void readPlaces() throws IOException {
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
    }

    private static void readPopulation() throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("population.txt").getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        while (line != null) {
            String[] split = line.split(";");
            if (split == null || split.length != 2)
                continue;
            populationMap.put(Integer.parseInt(split[0]), Long.parseLong(split[1]));
            line = reader.readLine();
        }
        reader.close();
    }

    private static void readDistance() throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("distance.txt").getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        while (line != null) {
            String[] split = line.split(",");
            if (split == null || split.length != 3)
                continue;
            String key = split[0] + split[1];
            distanceMap.put(key, Double.parseDouble(split[2]));
            line = reader.readLine();
        }
        reader.close();
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
                        matrix[i][j] = "0.0";
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
                            matrix[i][j] = (1.0 / count) + "";
                        } else {
                            matrix[i][j] = "0.0";
                        }

                    }


                }
            }

            String key = path.getSource().getId() + "" + path.getDestination().getId();
            if (path.getSource().getId() > path.getDestination().getId())
                key = path.getDestination().getId() + "" + path.getSource().getId();
            linkWeightMap.put(key, matrix);
            LINK_WEIGHT_MATRIXES.add(matrix);
            System.out.println("Path: " + path.getSource() + "-" + path.getDestination());
            printMatrix(matrix);
        }
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
