package com.mree.transportsample.util;

import java.util.Map;

public class PrintUtils {
    public static void printLinked(Map<String, String[][]> linkWeightMap) {
        System.out.println("Print Link Weight Map");
        for (String key : linkWeightMap.keySet()) {
            System.out.println("Key: " + key);
            printMatrix(linkWeightMap.get(key));
            System.out.println();
        }
    }

    public static void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printTrip(Map<String, Integer> tripMap) {
        System.out.println("Print Link Weight Map");
        for (String key : tripMap.keySet()) {
            System.out.println("Key: " + key);
            System.out.println("Trip Count: " + tripMap.get(key));
            System.out.println();
        }
    }

    public static void printFlow(Map<String, Double> flowGuessedMap) {
        System.out.println("Print Link Flow Map");
        for (String key : flowGuessedMap.keySet()) {
            System.out.println("Key: " + key);
            System.out.println("Flow: " + flowGuessedMap.get(key));
            System.out.println();
        }
    }
}
