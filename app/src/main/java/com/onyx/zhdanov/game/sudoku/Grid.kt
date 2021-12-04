package com.onyx.zhdanov.game.sudoku

import java.lang.StringBuilder

class Grid(private val grid: Array<IntArray>) {

    constructor(difficult: Int = 1) : this(generateNewGrid(difficult))

    fun checkSuccess(): Boolean {
        for (i in 0..8) {
            if (grid.getAvailableNumbersInColumn(i).isNotEmpty()) return false
            if (grid[i].getAvailableNumbers().isNotEmpty()) return false
        }
        for (i in 0..8 step 3) {
            for (j in 0..8 step 3) {
                if (grid.getAvailableNumbersInSection(i, j).isNotEmpty()) return false
            }
        }
        return true
    }

    fun findSolution(): Int {
        val allSolutions = findAllSolutions(grid)
        allSolutions.getOrNull(0)?.copyInto(grid)
        return allSolutions.size
    }

    override fun toString(): String {
        val buffer = StringBuilder()
        for (row in grid) {
            buffer.appendLine(row.joinToString())
        }
        return buffer.toString()
    }

    operator fun get(i: Int): IntArray {
        return grid[i]
    }

    companion object {
        const val FIELD_SIZE = 9

        private fun generateNewGrid(difficult: Int): Array<IntArray> {
            val grid = Array(FIELD_SIZE) { IntArray(FIELD_SIZE) { 0 } }
            fillGrid(grid)

            val fullCells = (0 until FIELD_SIZE * FIELD_SIZE).toMutableSet()
            var attempts = difficult
            while (attempts > 0) {
                val i = fullCells.random()
                val copyGrid = copyGrid(grid)
                copyGrid[i / FIELD_SIZE][i % FIELD_SIZE] = 0
                if (findAllSolutions(copyGrid).size != 1) {
                    attempts--
                } else {
                    grid[i / FIELD_SIZE][i % FIELD_SIZE] = 0
                    fullCells.remove(i)
                }
            }

            return grid
        }

        private fun fillGrid(grid: Array<IntArray>): Boolean {
            for (i in 0 until FIELD_SIZE * FIELD_SIZE) {
                val x = i % 9
                val y = i / 9

                if (grid[y][x] == 0) {
                    val column = grid.getAvailableNumbersInColumn(x)
                    val row = grid[y].getAvailableNumbers()
                    val section = grid.getAvailableNumbersInSection(x, y)

                    val availableNumbers = column.intersect(row).intersect(section).shuffled()

                    for (number in availableNumbers) {
                        grid[y][x] = number
                        if (checkGrid(grid)) {
                            return true
                        } else {
                            if (fillGrid(grid))
                                return true
                        }
                    }
                    grid[y][x] = 0
                    return false
                }
            }
            return true
        }

        private fun findAllSolutions(grid: Array<IntArray>, foundDecision: Int = 0): List<Array<IntArray>> {
            for (i in 0 until FIELD_SIZE * FIELD_SIZE) {
                val x = i % 9
                val y = i / 9

                if (grid[y][x] == 0) {
                    val column = grid.getAvailableNumbersInColumn(x)
                    val row = grid[y].getAvailableNumbers()
                    val section = grid.getAvailableNumbersInSection(x, y)

                    val availableNumbers = column.intersect(row).intersect(section)

                    var decisions = mutableListOf<Array<IntArray>>()
                    for (number in availableNumbers) {
                        grid[y][x] = number
                        if (checkGrid(grid)) {
                            val copyGrid = copyGrid(grid)
                            grid[y][x] = 0
                            return listOf(copyGrid)
                        } else {
                            decisions += findAllSolutions(grid, foundDecision)
                        }
                    }
                    grid[y][x] = 0
                    return decisions
                }
            }
            return listOf()
        }

        private fun copyGrid(grid: Array<IntArray>): Array<IntArray> {
            return grid.map { it.copyOf() }.toTypedArray()
        }

        private fun checkGrid(grid: Array<IntArray>): Boolean {
            for (row in grid) {
                if (row.contains(0)) return false
            }
            return true
        }
    }
}

private fun Array<IntArray>.getAvailableNumbersInColumn(ind: Int): Set<Int> {
    val availableList = (1..9).toMutableSet()
    for (row in this) {
        availableList.remove(row[ind])
    }
    return availableList
}

private fun Array<IntArray>.getAvailableNumbersInSection(x: Int, y: Int): Set<Int> {
    val availableList = (1..9).toMutableSet()
    val i = y / 3
    val j = x / 3
    for (row in 0..2) {
        for (column in 0..2) {
            availableList.remove(this[i * 3 + row][j * 3 + column])
        }
    }
    return availableList
}

private fun IntArray.getAvailableNumbers(): Set<Int> {
    val availableList = (1..9).toMutableSet()
    availableList.removeAll(this.asIterable())
    return availableList
}