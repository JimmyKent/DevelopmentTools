package com.jimmy.development.sudoku;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jinguochong on 2017/9/23.
 */

public class BlockTest {

    @Test
    public void testBlock() {

        for (int i = 0; i < 81; i++) {
            int[] lt = getLeftTop(i % 9, i / 9);
            System.out.println(i + " lt " + lt[0] + " , " + lt[1]);
        }

    }

    public static int[] getLeftTop(int x, int y) {
        int[] leftTop = new int[2];
        //找到块的左上角的点, 有9个(0,0) 9个(3,0) ...
        int blockX = x / 3 * 3;
        int blockY = y / 3 * 3;
        leftTop[0] = blockX;
        leftTop[1] = blockY;
        return leftTop;
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (input[blockX + i][blockY + j] != 0) {
                    impossible.add(input[blockX + i][blockY + j]);
                }
            }
        }*/
    }
}
