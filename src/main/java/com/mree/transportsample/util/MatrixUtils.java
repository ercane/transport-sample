package com.mree.transportsample.util;

public class MatrixUtils {

    public static String[][] sumMatrix(String[][] first, String[][] second) {
        int c = 0, d = 0;
        int m = first.length;
        String sum[][] = new String[m][m];

        for (c = 0; c < m; c++) {
            for (d = 0; d < m; d++) {
                Double result = (Double.parseDouble(first[c][d]) + Double.parseDouble(second[c][d]));
                sum[c][d] = "" + result;
            }
        }
        return sum;
    }

    public static String[][] multiplyWithNumber(String[][] first, Double number) {
        int c = 0, d = 0;
        int m = first.length;
        String multiply[][] = new String[m][m];

        for (c = 0; c < m; c++) {
            for (d = 0; d < m; d++) {
                Double result = Double.parseDouble(first[c][d]) * number;
                multiply[c][d] = "" + result;
            }
        }
        return multiply;
    }
}
