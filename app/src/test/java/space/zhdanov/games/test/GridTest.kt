package space.zhdanov.games.test

import android.graphics.Point
import com.onyx.zhdanov.game.sudoku.Coordinate
import com.onyx.zhdanov.game.sudoku.Grid
import org.junit.Test

import org.junit.Assert.*

class GridTest {
    @Test
    fun `must generate right full grid with 1 solution`() {
        val grid = Grid()
        println(grid.toString())
        assertEquals(1, grid.findSolution())
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

    @Test
    fun `must find 2 solutions`() {
        val grid = Grid(arrayOf(
            intArrayOf(0, 9, 0, 2, 4, 3, 8, 0, 0),
            intArrayOf(0, 8, 4, 0, 0, 0, 2, 9, 3),
            intArrayOf(0, 0, 3, 0, 8, 0, 0, 5, 0),

            intArrayOf(4, 6, 0, 0, 0, 2, 0, 0, 7),
            intArrayOf(8, 0, 0, 3, 0, 0, 0, 1, 0),
            intArrayOf(9, 0, 2, 8, 0, 7, 5, 0, 0),

            intArrayOf(0, 0, 0, 4, 0, 6, 7, 0, 8),
            intArrayOf(0, 7, 0, 0, 2, 1, 4, 6, 0),
            intArrayOf(0, 0, 6, 7, 0, 0, 0, 3, 0),
        ))
        val solutions = grid.findSolution()
        assertEquals(2, solutions)
    }

    @Test
    fun `must find 1 solution`() {
        val grid = Grid(arrayOf(
            intArrayOf(0, 3, 0, 0, 0, 0, 0, 0, 8),
            intArrayOf(0, 8, 4, 0, 3, 5, 1, 0, 7),
            intArrayOf(0, 0, 0, 0, 0, 8, 0, 4, 3),

            intArrayOf(4, 2, 0, 8, 0, 0, 0, 5, 0),
            intArrayOf(5, 0, 3, 9, 0, 6, 0, 0, 0),
            intArrayOf(6, 0, 0, 5, 0, 0, 0, 7, 4),

            intArrayOf(0, 5, 0, 0, 8, 0, 4, 1, 2),
            intArrayOf(0, 0, 0, 3, 5, 7, 9, 8, 6),
            intArrayOf(0, 0, 9, 0, 0, 1, 0, 0, 0),
        ))
        val solutions = grid.findSolution()
        assertEquals(1, solutions)
    }

    @Test
    fun `must return list of mistakes`() {
        val grid = Grid(arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 5, 7, 8, 9),
            intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 0, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 3),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        ))
        assertEquals(setOf(
            Coordinate(4, 0),
            Coordinate(5, 0),
            Coordinate(5,8),
            Coordinate(8,1),
            Coordinate(8,7),
            Coordinate(6,7)
        ), grid.getMistakes())
    }

    @Test
    fun `must return empty list of mistakes for not full grid`() {
        val grid = Grid(arrayOf(
            intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            intArrayOf(5, 6, 7, 8, 0, 1, 2, 3, 4),
            intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            intArrayOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 0),
            intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        ))
        assertEquals(setOf<Coordinate>(), grid.getMistakes())
    }

    @Test
    fun `must return empty list of mistakes for full success grid`() {
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
        assertEquals(setOf<Coordinate>(), grid.getMistakes())
    }
}