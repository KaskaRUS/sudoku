package space.zhdanov.games.test

import com.onyx.zhdanov.game.sudoku.Grid
import org.junit.Test

import org.junit.Assert.*

class GridTest {
    @Test
    fun `must generate right full grid`() {
        val grid = Grid()
        println(grid.toString())
        assertTrue(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return true for full success grid`() {
        val grid = Grid(arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        ))
        assertTrue(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return false for wrong grid`() {
        val grid = Grid(arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            intArrayOf(8, 5, 6, 7, 4, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 3),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        ))
        assertFalse(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return false for not full grid`() {
        val grid = Grid(arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 0, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 3),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        ))
        assertFalse(grid.checkSuccess())
    }
}