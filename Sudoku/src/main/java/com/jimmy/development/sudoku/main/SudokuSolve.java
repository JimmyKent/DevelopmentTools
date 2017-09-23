package com.jimmy.development.sudoku.main;

/**
 * Created by jinguochong on 2017/9/22.
 */

public class SudokuSolve {

    public static void main(String[] args) {


        String[] string = { "..9748...", "7........", ".2.1.9...", "..7...24.", ".64.1.59.", ".98...3..", "...8.3.2.",

                "........6", "...2759.." };

        char[][] board = new char[9][9];

// 初始化数独

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                board[i][j] = string[i].charAt(j);

            }

        }

        new SudokuSolve().solveSudoku(board);

    }

    public void solveSudoku(char[][] board) {

        if (board == null || board.length != 9 || board[0].length != 9) {

            return;

        }

        if (solve(board, 0, 0)) {

// 打印结果

            for (int i = 0; i < 9; i++) {

                for (int j = 0; j < 9; j++) {

                    System.out.print(board[i][j] + " ");

                }

                System.out.println();

            }

        }

    }

    public boolean solve(char[][] board, int i, int j) {

        if (j == 9) {

            if (i == 8)

                return true;

            i++;

            j = 0;

        }

        if (board[i][j] != '.') {

            return solve(board, i, j + 1);

        }

        for (char k = '1'; k <= '9'; k++) {

            if (isValid(board, i, j, k)) {

                board[i][j] = k;

                if (solve(board, i, j + 1))

                    return true;

                else

                    board[i][j] = '.';

            }

        }

        return false;

    }

    public boolean isValid(char[][] board, int i, int j, char c) {

        for (int k = 0; k < 9; k++) {

            if (board[i][k] != '.' && board[i][k] == c)

                return false;

            if (board[k][j] != '.' && board[k][j] == c)

                return false;

            if (board[i / 3 * 3 + k / 3][j / 3 * 3 + k % 3] != '.'

                    && board[i / 3 * 3 + k / 3][j / 3 * 3 + k % 3] == c)

                return false;

        }

        return true;

    }

}
