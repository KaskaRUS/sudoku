package com.onyx.zhdanov.game.sudoku.models

import java.lang.StringBuilder

class Grid(private val grid: Array<IntArray>, private val onSuccess: (score: Int) -> Unit) {
    var mistakes: Set<Coordinate> = setOf()

    constructor(difficult: Int = 1, onSuccess: (score: Int) -> Unit) : this(generateNewGrid(difficult), onSuccess)

    private val firstRepresentation = grid.map { it.copyOf() }.toTypedArray()

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

    private fun findMistakes(): Set<Coordinate> {
        val columns = (0 until FIELD_SIZE).flatMap { column ->
            grid.getDuplicatesIndexesInColumn(column)
                .map { row -> Coordinate(column, row) }
        }
        val row = (0 until FIELD_SIZE).flatMap { row ->
            grid[row].getDuplicatesIndexes()
                .map { column -> Coordinate(column, row) }
        }
        val sections = (0..2).flatMap { sectionX ->
            (0..2).flatMap { sectionY ->
                grid.getDuplicatesIndexesInSection(sectionX, sectionY)
            }
        }
        return  (columns + row + sections).toSet()
    }

    fun findSolution(): Int {
        val allSolutions = findAllSolutions(grid)
        allSolutions.getOrNull(0)?.copyInto(grid)
        return allSolutions.size
    }

    fun isStartedCell(x: Int, y: Int): Boolean =
        firstRepresentation[y][x] != 0

    fun changeCell(x: Int, y: Int, newValue: Int): Boolean {
        return if (isStartedCell(x, y)) {
            return false
        } else {
            grid[y][x] = newValue
            mistakes = findMistakes()
            if (checkSuccess()) {
                onSuccess(0)
            }
            true
        }
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
                if (findAllSolutions(copyGrid).size == 1) {
                    grid[i / FIELD_SIZE][i % FIELD_SIZE] = 0
                    fullCells.remove(i)
                }
                attempts--
            }

            return grid
        }

        private fun fillGrid(grid: Array<IntArray>, startPosition: Int = 0): Boolean {
            for (i in startPosition until FIELD_SIZE * FIELD_SIZE) {
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
                            if (fillGrid(grid, i + 1))
                                return true
                        }
                    }
                    grid[y][x] = 0
                    return false
                }
            }
            return true
        }

        private fun findAllSolutions(grid: Array<IntArray>, foundDecision: Int = 0, startPosition: Int = 0): List<Array<IntArray>> {
            for (i in startPosition until FIELD_SIZE * FIELD_SIZE) {
                val x = i % 9
                val y = i / 9

                if (grid[y][x] == 0) {
                    val column = grid.getAvailableNumbersInColumn(x)
                    val row = grid[y].getAvailableNumbers()
                    val section = grid.getAvailableNumbersInSection(x, y)

                    val availableNumbers = column.intersect(row).intersect(section)

                    val decisions = mutableListOf<Array<IntArray>>()
                    for (number in availableNumbers) {
                        grid[y][x] = number
                        if (checkGrid(grid)) {
                            val copyGrid = copyGrid(grid)
                            grid[y][x] = 0
                            return listOf(copyGrid)
                        } else {
                            decisions += findAllSolutions(grid, foundDecision, i + 1)
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

private fun Array<IntArray>.getDuplicatesIndexesInColumn(ind: Int): List<Int> {
    val list = this.map { it[ind] }
    return list.mapIndexedNotNull { index, value ->
        if (value > 0 && list.filterIndexed { ind, i -> ind != index && i == value }.isNotEmpty()) index else null
    }
}

private fun IntArray.getDuplicatesIndexes(): List<Int> {
    val list = this.toList()
    return list.mapIndexedNotNull { index, value ->
        if (value > 0 && list.filterIndexed { ind, i -> ind != index && i == value }.isNotEmpty()) index else null
    }
}

private fun Array<IntArray>.getDuplicatesIndexesInSection(sectionX: Int, sectionY: Int): List<Coordinate> {
    val list = (0..8).map { ind -> this[sectionY * 3 + ind / 3][sectionX * 3 + ind % 3] }

    return list.mapIndexedNotNull { index, value ->
        if (value > 0 && list.filterIndexed { ind, i -> ind != index && i == value }.isNotEmpty()) {
            Coordinate(index % 3 + sectionX * 3, index / 3 + sectionY * 3)
        } else null
    }
}

data class Coordinate(
    val x: Int,
    val y: Int
)