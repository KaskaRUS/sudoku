package space.zhdanov.games.test

import com.onyx.zhdanov.game.sudoku.models.Coordinate
import com.onyx.zhdanov.game.sudoku.models.Field.Companion.FIELD_SIZE
import com.onyx.zhdanov.game.sudoku.models.Grid
import com.onyx.zhdanov.game.sudoku.utils.array2dOf
import org.junit.Assert.*
import org.junit.Test

class GridTest {
    @Test
    fun `must generate right full grid with 1 solution`() {
        val grid = Grid(difficult = 10) {}
        println(grid.toString())
        assertEquals(1, grid.findSolution())
        println(grid.toString())
        assertTrue(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return true for full success grid`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9,
                    4, 5, 6, 7, 8, 9, 1, 2, 3,
                    7, 8, 9, 1, 2, 3, 4, 5, 6,
                    2, 3, 4, 5, 6, 7, 8, 9, 1,
                    5, 6, 7, 8, 9, 1, 2, 3, 4,
                    8, 9, 1, 2, 3, 4, 5, 6, 7,
                    3, 4, 5, 6, 7, 8, 9, 1, 2,
                    6, 7, 8, 9, 1, 2, 3, 4, 5,
                    9, 1, 2, 3, 4, 5, 6, 7, 8,
                )
            }
        ) {}
        assertTrue(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return false for wrong grid`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9,
                    8, 5, 6, 7, 4, 9, 1, 2, 3,
                    7, 8, 9, 1, 2, 3, 4, 5, 6,
                    2, 3, 4, 5, 6, 7, 8, 9, 1,
                    5, 6, 7, 8, 9, 1, 2, 3, 4,
                    8, 9, 1, 2, 3, 4, 5, 6, 7,
                    3, 4, 5, 6, 7, 8, 9, 1, 2,
                    6, 7, 8, 9, 1, 2, 3, 4, 3,
                    9, 1, 2, 3, 4, 5, 6, 7, 8,
                )
            }) {}
        assertFalse(grid.checkSuccess())
    }

    @Test
    fun `must checkSuccess return false for not full grid`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9,
                    4, 5, 6, 7, 8, 9, 1, 2, 3,
                    7, 8, 9, 1, 2, 3, 4, 5, 6,
                    2, 3, 4, 5, 6, 7, 8, 9, 1,
                    5, 6, 7, 8, 0, 1, 2, 3, 4,
                    8, 9, 1, 2, 3, 4, 5, 6, 7,
                    3, 4, 5, 6, 7, 8, 9, 1, 2,
                    6, 7, 8, 9, 1, 2, 3, 4, 3,
                    9, 1, 2, 3, 4, 5, 6, 7, 8,
                )
            }
        ) {}
        assertFalse(grid.checkSuccess())
    }

    @Test
    fun `must find 2 solutions`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    0, 9, 0, 2, 4, 3, 8, 0, 0,
                    0, 8, 4, 0, 0, 0, 2, 9, 3,
                    0, 0, 3, 0, 8, 0, 0, 5, 0,

                    4, 6, 0, 0, 0, 2, 0, 0, 7,
                    8, 0, 0, 3, 0, 0, 0, 1, 0,
                    9, 0, 2, 8, 0, 7, 5, 0, 0,

                    0, 0, 0, 4, 0, 6, 7, 0, 8,
                    0, 7, 0, 0, 2, 1, 4, 6, 0,
                    0, 0, 6, 7, 0, 0, 0, 3, 0,
                )
            }
        ) {}
        val solutions = grid.findSolution()
        assertEquals(2, solutions)
    }

    @Test
    fun `must find 1 solution`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    0, 3, 0, 0, 0, 0, 0, 0, 8,
                    0, 8, 4, 0, 3, 5, 1, 0, 7,
                    0, 0, 0, 0, 0, 8, 0, 4, 3,

                    4, 2, 0, 8, 0, 0, 0, 5, 0,
                    5, 0, 3, 9, 0, 6, 0, 0, 0,
                    6, 0, 0, 5, 0, 0, 0, 7, 4,

                    0, 5, 0, 0, 8, 0, 4, 1, 2,
                    0, 0, 0, 3, 5, 7, 9, 8, 6,
                    0, 0, 9, 0, 0, 1, 0, 0, 0,
                )
            }
        ) {}
        val solutions = grid.findSolution()
        assertEquals(1, solutions)
    }

    @Test
    fun `must return list of mistakes`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3,  4, 5, 5,  7, 8, 9,
                    4, 5, 6,  7, 8, 9,  1, 2, 3,
                    7, 8, 9,  1, 2, 3,  4, 5, 6,

                    2, 3, 4,  5, 6, 7,  8, 9, 1,
                    5, 6, 7,  8, 0, 1,  2, 3, 4,
                    8, 9, 1,  2, 3, 4,  5, 6, 7,

                    3, 4, 5,  6, 7, 8,  9, 1, 2,
                    6, 7, 8,  9, 1, 2,  3, 4, 3,
                    9, 1, 2,  3, 4, 5,  6, 7, 8,
                )
            }
        ) {}
        assertEquals(
            setOf(
                Coordinate(4, 0),
                Coordinate(5, 0),
                Coordinate(5, 8),
                Coordinate(8, 1),
                Coordinate(8, 7),
                Coordinate(6, 7)
            ), grid.mistakes
        )
    }

    @Test
    fun `must return empty list of mistakes for not full grid`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9,
                    4, 5, 6, 7, 8, 9, 1, 2, 3,
                    7, 8, 9, 1, 2, 3, 4, 5, 6,
                    2, 3, 4, 5, 6, 7, 8, 9, 1,
                    5, 6, 7, 8, 0, 1, 2, 3, 4,
                    8, 9, 1, 2, 3, 4, 5, 6, 7,
                    3, 4, 5, 6, 7, 8, 9, 1, 2,
                    6, 7, 8, 9, 1, 2, 3, 4, 0,
                    9, 1, 2, 3, 4, 5, 6, 7, 8,
                )
            }
        ) {}
        assertEquals(setOf<Coordinate>(), grid.mistakes)
    }

    @Test
    fun `must return empty list of mistakes for full success grid`() {
        val grid = Grid(
            array2dOf(FIELD_SIZE, FIELD_SIZE) {
                intArrayOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9,
                    4, 5, 6, 7, 8, 9, 1, 2, 3,
                    7, 8, 9, 1, 2, 3, 4, 5, 6,
                    2, 3, 4, 5, 6, 7, 8, 9, 1,
                    5, 6, 7, 8, 9, 1, 2, 3, 4,
                    8, 9, 1, 2, 3, 4, 5, 6, 7,
                    3, 4, 5, 6, 7, 8, 9, 1, 2,
                    6, 7, 8, 9, 1, 2, 3, 4, 5,
                    9, 1, 2, 3, 4, 5, 6, 7, 8,
                )
            }
        ) {}
        assertEquals(setOf<Coordinate>(), grid.mistakes)
    }
}