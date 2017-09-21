package com.jimmy.development.sudoku;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/21.
 */

public class SudokuTest {

    private static int[][] sudoku = new int[9][9];

    private static String test = //
            "360000000" +
                    "004230800" +
                    "000004200" +
                    "070460003" +
                    "820000014" +
                    "500013020" +
                    "001900000" +
                    "007048300" +
                    "000000045";

    public static void main(String[] args) {
        char[] chars = test.toCharArray();
        System.out.println(new String(chars));
        int[][] arr = new int[9][9];

        int i = 0, j = 0;
        for (char c : chars) {
            if (j < 9) {
                arr[i][j] = c - 48;
                j++;
            } else {
                j = 0;
                i++;
                arr[i][j] = c - 48;
            }
        }
        cal(arr);

        print(arr);

    }

    private static void cal(int[][] input){

    }


    private static void initSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = 0;
            }
        }
    }

    private static void print(int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(arr[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
}