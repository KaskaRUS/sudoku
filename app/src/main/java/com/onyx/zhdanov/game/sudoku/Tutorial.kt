package com.onyx.zhdanov.game.sudoku

import android.graphics.Bitmap

/*
1. Show rules of games:
    a. You have to fill all cells in field
    b. In sections have to be only digit 1 to 9 unique
    c. In columns have to be only digit 1 to 9 unique
    d. In rows have to be only digit 1 to 9 unique
2. Write digit
3. Add mistake
4. Remove digit
5. Back to menu
6. Complete field
 */

fun tutorialFieldGrid() = arrayOf(
    intArrayOf(1, 0, 3, 4, 5, 6, 7, 8, 9),
    intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
    intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
    intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
    intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
    intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
    intArrayOf(3, 4, 5, 6, 7, 8, 9, 0, 2),
    intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
    intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
)