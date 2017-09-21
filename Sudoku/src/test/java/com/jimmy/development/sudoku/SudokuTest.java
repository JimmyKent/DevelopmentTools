package com.jimmy.development.sudoku;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jinguochong on 2017/9/21.
 * 数独
 */

public class SudokuTest {

    private static int[][] sudoku = new int[9][9];

    private static String test =
            "" +
                    "360000000" +
                    "004230800" +
                    "000004200" +
                    "070460003" +
                    "820000014" +
                    "500013020" +
                    "001900000" +
                    "007048300" +
                    "000000045";

    @Test
    public void main() {
        char[] chars = test.toCharArray();
        //System.out.println(new String(chars));

        int[][] arr = init(chars);

        print(arr);

        cal(arr);


    }

    private static int[][] init(char[] chars) {

        int[][] arr = new int[9][9];
        int i = 0, j = 0;

        int zeroSum = 0;
        int value;

        for (char c : chars) {
            if (j < 9) {
                value = c - 48;
                arr[i][j] = value;
                j++;
            } else {
                j = 0;
                i++;
                value = c - 48;
                arr[i][j] = value;
            }
            if (value == 0) {
                zeroSum++;
            }
        }

        System.out.println("Zero sum : " + zeroSum);

        return arr;
    }

    private static void cal(int[][] input) {

        long time = System.currentTimeMillis();
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = new Cell();
                cell.value = input[i][j];
                cell.position = i * 9 + j;
                cell.calImpossible(input);
                cells.add(cell);
                //System.out.println(cell.toString());
            }
        }

        //here is correct


        // 从 0-80 遍历, 触发calImpossible
        //a. if (c.value == 0) {
        //b. c.impossible.size() == 8
        //可以得到 data 刷新, 然后重新从0开始再次遍历, 触发calImpossible

        int i = 0;
        boolean dataChanged = true;
        while (dataChanged) {
            dataChanged = false;

            for (; i < 81; i++) {
                Cell c = cells.get(i);
                if (c.value == 0) {
                    if (c.impossible.size() == 8) {
                        System.out.println(c.toString());
                        int sum = 0;
                        for (int j : c.impossible) {
                            sum += j;
                        }
                        c.value = Cell.TOTAL - sum;
                        int x = c.position % 9;//行
                        int y = c.position / 9;//列
                        input[y][x] = c.value;
                        //System.out.println("dataChanged : " + "(" + x + "," + y + ")  " + c.value);
                        dataChanged = true;
                        i = 0;
                    }
                }
                c.calImpossible(input);
            }
        }

        print(input);

        System.out.println("Cost time : " + (System.currentTimeMillis() - time));

    }

    //when create sudoku
    private static void initSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudoku[i][j] = 0;
            }
        }
    }

    private static void print(int[][] arr) {
        System.out.println("┌───────┬───────┬───────┐");
        for (int i = 0; i < 9; i++) {
            System.out.print("│ ");
            for (int j = 0; j < 9; j++) {
                System.out.print(arr[i][j] + " ");
                if (j == 2 | j == 5)
                    System.out.print("│ ");
            }
            System.out.print("│");
            System.out.print("\n");
            if (i == 2 | i == 5)
                System.out.println("├───────┼───────┼───────┤");
        }
        System.out.println("└───────┴───────┴───────┘");
    }

    private static void print2(int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(arr[i][j]).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }


    private static class Cell {
        public static final int TOTAL = (1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9);

        /**
         * //在数独中的位置 [0, 80)
         */
        private int position;//在数独中的位置 [0, 80)
        private int value;//确认值
        private Set<Integer> impossible = new HashSet<>();

        private List<Integer> line = new ArrayList<>();//对应的行存在的
        private List<Integer> column = new ArrayList<>();//对应的列存在的
        private List<Integer> block = new ArrayList<>();//对应的块存在的


        public void calImpossible(int[][] input) {
            if (value != 0) {
                return;
            }
            int x = position % 9;//行
            int y = position / 9;//列


            for (int i = 0; i < 9; i++) {
                //找出行已经有的数

                if (input[y][i] != 0) {
                    line.add(input[y][i]);
                    impossible.add(input[y][i]);
                }
                //找出列已经有的数
                if (input[i][x] != 0) {
                    column.add(input[i][x]);
                    impossible.add(input[i][x]);
                }
            }

            //找出块已经有的数

            //找到块的左上角的点, 有9个(0,0) 9个(3,0) ...
            int blockX = x / 3 * 3;
            int blockY = y / 3 * 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (input[blockX + i][blockY + j] != 0) {
                        block.add(input[blockX + i][blockY + j]);
                        impossible.add(input[blockX + i][blockY + j]);
                    }
                }
            }

        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Cell : ").append(position).append(" ");
            sb.append("value : ").append(value).append("  ");
            sb.append("impossible : ");
            for (int i : impossible) {
                sb.append(i).append("  ");
            }
            /*sb.append("\n").append("line : ");
            for (int i : line) {
                sb.append(i).append("  ");
            }
            sb.append("\n").append("column : ");
            for (int i : column) {
                sb.append(i).append("  ");
            }
            sb.append("\n").append("block : ");
            for (int i : block) {
                sb.append(i).append("  ");
            }*/
            return sb.toString();
        }

        private void addProbability(int i) {
            impossible.add(i);
        }

        private void removeProbability(int i) {
            if (impossible.contains(i)) {
                impossible.remove(i);
            }
        }


    }

    @Test
    public void testCell() {//ok
        Cell cell = new Cell();
        cell.addProbability(2);
        cell.addProbability(1);
        cell.addProbability(5);
        cell.removeProbability(0);

        System.out.println(Collections.singletonList(cell.impossible));
    }

    @Test
    public void testPosition() {
        for (int i = 0; i < 81; i++) {
            getPos(i, sudoku);
        }

    }

    private static void getPos(int position, int[][] input) {
        int x = position % 9;//行
        int y = position / 9;//列


        //找出块已经有的数
        int blockX = x % 3;
        int blockY = y % 3;
        int blockX2 = x / 3 * 3;
        int blockY2 = y / 3 * 3;
        //int position = blockY * 3 + blockX;
        //应该有9个(0,0) 9个(3,0) ...

        //System.out.println((x - blockX) + ", " + (y - blockY));
        System.out.println(blockX2 + ", " + blockY2);
        //int[] firstPos = {x - blockX, y - blockX};//找到块的左上角的点
    }

}