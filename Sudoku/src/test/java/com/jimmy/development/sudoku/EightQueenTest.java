package com.jimmy.development.sudoku;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jinguochong on 17-11-20.
 */

public class EightQueenTest {

    private int[] existPos = new int[8];//

    @Test
    public void main() {
        for (int i = 0; i < existPos.length; i++) {
            existPos[i] = -1;
        }
        cal(0, false);
    }


    @Test
    public void testPosValid() {
        boolean valid = posValid(63, 0);
        Assert.assertEquals(valid, false);

    }

    private void cal(int pos, boolean isRevert) {

        int x = pos % 8;
        int y = pos / 8;

        if (y == 7 && x == 8) {
            return;
        }
        if (isRevert) {
            if (x == 7) {
                int prev = existPos[y];
                existPos[y] = -1;
                cal(++prev, true);
                return;
            }
            //pos++;
        }

        x = pos % 8;
        y = pos / 8;

        while (pos < 64) {
            if (isValid(pos)) {
                existPos[y] = pos;
                print(existPos);
                x = 0;
                y++;
                pos = y * 8 + x;
            } else {
                x = pos % 8;
                y = pos / 8;
                if (x == 7) {
                    y--;
                    System.out.println("y =" + y);
                    int prev = existPos[y];
                    System.out.println("prev =" + prev);
                    existPos[y] = -1;
                    print(existPos);
                    cal(++prev, true);
                    return;
                }
                pos++;
            }

        }

        print(existPos);

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

    private static void print(int[] arr) {
        int[][] towArr = new int[8][8];
        for (int i : arr) {
            i = i < 0 ? 0 : i;
            towArr[i / 8][i % 8] = 1;
        }
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
