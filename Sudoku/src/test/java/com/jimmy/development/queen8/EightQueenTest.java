package com.jimmy.development.queen8;

import org.junit.Test;

/**
 * Created by jinguochong on 17-11-20.
 */

public class EightQueenTest {

    private int[] existPos = new int[8];//
    private static int count;

    @Test
    public void main() {
        for (int i = 0; i < existPos.length; i++) {
            existPos[i] = -1;
        }
        cal(0);
    }


    /*@Test
    public void testPosValid() {
        boolean valid = posValid(63, 0);
        Assert.assertEquals(valid, false);

    }*/


    /**
     * 每层递归设一个指针，从左往右，如果找到合法的就赋值，往下进行
     * 产生回溯的时候，重置当前指针位置
     */
    private void cal(int pos) {
        int x = pos % 8;
        final int Y = pos / 8;

        if (x == -1) {
            return;
        }
        if (Y == 8) {
            print(existPos);

            int last = existPos[Y - 1];
            existPos[Y - 1] = -1;
            if (last % 8 == 7) {
                last = existPos[Y - 2];
                existPos[Y - 2] = -1;
            }
            cal(++last);
            return;
        }

        while (x < 8) {
            if (isValid(pos)) {
                existPos[Y] = pos;

                int next = 8 * (Y + 1);
                cal(next);//前进
                return;
            } else {
                x++;
                pos = x + Y * 8;
            }
        }
        //回溯
        //if (x == 8) {
        int last = existPos[Y - 1];
        existPos[Y - 1] = -1;
        if (last % 8 == 7) {
            if (Y - 2 == -1) {
                return;
            }
            last = existPos[Y - 2];
            existPos[Y - 2] = -1;
        }
        cal(++last);
        //}
    }

    private boolean isValid(int pos) {
        boolean valid = true;
        for (int i = 0; i < existPos.length; i++) {
            int queen = existPos[i];
            if (queen != -1) {
                valid = posValid(pos, queen);
                if (!valid) {
                    return false;
                }
            }
        }


        return valid;
    }

    private boolean posValid(int pos, int queen) {
        int x = queen % 8;
        int y = queen / 8;

        int posX = pos % 8;
        int posY = pos / 8;
        if (posX == x) {
            return false;
        }
        if (posY == y) {
            return false;
        }

        int leftDiagonal = x - (posY - y) + 8 * posY;
        if (leftDiagonal == pos) {
            return false;
        }

        int rightDiagonal = x + (posY - y) + 8 * posY;
        if (rightDiagonal == pos) {
            return false;
        }

        return true;
    }

    private void print(int[] arr) {
        count++;
        System.out.println(count);
        int[][] towArr = new int[8][8];
        StringBuilder sb = new StringBuilder();
        for (int i : arr) {
            i = i < 0 ? 0 : i;
            sb.append(i).append(" ");
            towArr[i / 8][i % 8] = 1;
        }
        System.out.println(sb);
        print(towArr);
    }

    private static void print(int[][] arr) {
        System.out.println("┌─────────────────┐");
        for (int i = 0; i < 8; i++) {
            System.out.print("│ ");
            for (int j = 0; j < 8; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.print("│");
            System.out.println("");
        }
        System.out.println("└─────────────────┘");
    }

}
