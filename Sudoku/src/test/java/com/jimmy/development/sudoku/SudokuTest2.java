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

public class SudokuTest2 {

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

    private static List<Integer> TOTAL = new ArrayList<>();
    private static final int NUM_MAX = 9;
    private static final int POS_MAX = 80;


    //@Test
    public static void main(String[] args) {
        char[] chars = test.toCharArray();
        //System.out.println(new String(chars));
        initConstants();

        int[][] arr = init(chars);

        print(arr);

        //cal(arr);

        //backTrack(arr);

        backTrack(arr, 0, 0, false);

    }

    private static void initConstants() {
        for (int i = 1; i < 10; i++) {
            TOTAL.add(i);
        }
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

    /**
     * 回溯法求解:
     * 认为数独位置有序;
     * guess 起点有两种:
     * a.从0一直往后遍历
     * b.从possible最少的开始遍历
     * <p>
     * 选b:
     * 维护List<Cell>
     * cell.possible 只由value改变才进行改变, 不随guess改变而变,
     * Cell添加guess list, 如果产生回溯, 删除guess list 的最后一个元素
     * 维护的东西太多了, 不好回溯, 需要牺牲太多内存在存各种状态的List
     * <p>
     * 选a:
     * 暴力求解的略微改进;
     * guess从小到大进行
     * 思路:从0-80 顺序guess, 不需要维护guess表, 产生guess之后, 立即计算下一个位置的可能.
     * 如果产生回溯, 只需把当前guess的值++,
     *
     * @param input 原始数独
     */
    private static void backTrack(int[][] input, int position, int currentGuess, boolean isRecursion) {

        if (position > 80) {
            return;
        }

        int x = position % 9;//行
        int y = position / 9;//列


        // recursion
        if (isRecursion) {
            int guessNext = guessNext(input, position, currentGuess);

            System.out.println("recursion : " + position + "  " + currentGuess +  "  " + guessNext);

            if (guessNext == 0) { //回溯
                input[y][x] = 0;
                --position;
                int prevGuess = input[position / 9][position % 9];
                backTrack(input, position, prevGuess, true);
            } else {
                input[y][x] = guessNext;
                position++;
                backTrack(input, position, 0, false);
            }
            return;
        }

        // forward
        int value = input[y][x];

        if (value == 0) {
            int guessNext = guessNext(input, position, currentGuess);

            System.out.println("forward : " + position + "   " + currentGuess + "  " + guessNext);

            if (guessNext == 0) { //回溯
               // input[y][x] = 0;
                --position;
                int prevGuess = input[position / 9][position % 9];
                backTrack(input, position, prevGuess, true);
            } else {
                input[y][x] = guessNext;
                position++;
                backTrack(input, position, 0, false);
            }
        } else {
            position++;

            backTrack(input, position, 0, false);//prevGuess
        }
    }

    /**
     * @return 返回下一个猜测的点
     */
    private static int guessNext(int[][] input, int position, int current) {
        int next = 0;

        /*if (current == NUM_MAX) {
            return next;
        }*/

        Set<Integer> impossible = new HashSet<>();

        int x = position % 9;//行
        int y = position / 9;//列

        /*int value = input[y][x];

        if (value != 0) {
            return next;
        }*/

        for (int i = 0; i < 9; i++) {
            //找出行已经有的数
            if (input[y][i] != 0) {
                impossible.add(input[y][i]);
            }
            //找出列已经有的数
            if (input[i][x] != 0) {
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
                    impossible.add(input[blockX + i][blockY + j]);
                }
            }
        }


        List<Integer> possible = new ArrayList<>();
        possible.addAll(TOTAL);

        //System.out.println(Collections.singletonList(possible));

        possible.removeAll(impossible);

        Collections.sort(possible);
        for (int i : possible) {
            if (i > current) {
                return i;
            }
        }

        return next;
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


    @Test
    public void testGuessNext() {

        int next = 0;

        Set<Integer> impossible = new HashSet<>();
        impossible.add(1);
        impossible.add(2);
        impossible.add(3);
        impossible.add(4);
        impossible.add(5);
        impossible.add(6);
        impossible.add(7);
        impossible.add(8);
        impossible.add(9);

        int current = 4;

        List<Integer> temp = new ArrayList<>();
        temp.addAll(TOTAL);

        temp.removeAll(impossible);
        Collections.sort(temp);

        for (int i : temp) {
            if (i > current) {
                next = i;
                break;
            }
        }

        System.out.println(next);
    }


}