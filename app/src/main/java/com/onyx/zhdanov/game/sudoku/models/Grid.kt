package com.onyx.zhdanov.game.sudoku.models

import com.onyx.zhdanov.game.sudoku.utils.Array2D

class Grid(val grid: Array2D, private val onSuccess: (score: Int) -> Unit) {
    var mistakes: Set<Coordinate> = findMistakes()

    constructor(difficult: Int = 1, onSuccess: (score: Int) -> Unit) : this(generateNewGrid(difficult), onSuccess)

    val firstRepresentation = grid.clone()

    fun loadUserSolution(solutionGrid: Array<IntArray>) {
        solutionGrid.forEachIndexed { y, line ->
            line.forEachIndexed { x, cell ->
                if (cell > 0) {
                    grid[x, y] = cell
                }
            }
        }
    }

    fun findSolution(): Int {
        val allSolutions = findAllSolutions(grid)
        allSolutions.getOrNull(0)?.copyInto(grid)
        return allSolutions.size
    }

    fun isStartedCell(x: Int, y: Int): Boolean =
        firstRepresentation[x, y] != 0

    fun changeCell(x: Int, y: Int, newValue: Int): Boolean {
        return if (isStartedCell(x, y)) {
            return false
        } else {
            grid[x, y] = newValue
            mistakes = findMistakes()
            if (checkSuccess()) {
                onSuccess(0)
            }
            true
        }
    }

    override fun toString(): String = grid.toString()

    private fun findMistakes(): Set<Coordinate> {
        val columns = (0 until FIELD_SIZE).flatMap { column ->
            grid[column].getDuplicatesIndexes()
                .map { row -> Coordinate(column, row) }
        }
        val row = (0 until FIELD_SIZE).flatMap { row ->
            grid[Unit, row].getDuplicatesIndexes()
                .map { column -> Coordinate(column, row) }
        }
        val sections = (0..2).flatMap { sectionX ->
            (0..2).flatMap { sectionY ->
                grid.getDuplicatesIndexesInSection(sectionX, sectionY)
            }
        }
        return  (columns + row + sections).toSet()
    }

    fun checkSuccess(): Boolean {
        for (i in 0..8) {
            if (grid[Unit, i].getAvailableNumbers().isNotEmpty()) return false
            if (grid[i].getAvailableNumbers().isNotEmpty()) return false
        }
        for (i in 0..8 step 3) {
            for (j in 0..8 step 3) {
                if (grid.getAvailableNumbersInSection(i, j).isNotEmpty()) return false
            }
        }
        return true
    }

    companion object {
        const val FIELD_SIZE = 9

        private fun generateNewGrid(difficult: Int): Array2D {
            val grid = Array2D(FIELD_SIZE, FIELD_SIZE, 0)
            fillGrid(grid)

            val fullCells = (0 until FIELD_SIZE * FIELD_SIZE).toMutableSet()
            var attempts = difficult
            while (attempts > 0) {
                val i = fullCells.random()
                val copyGrid = grid.clone()
                val x = i % FIELD_SIZE
                val y = i / FIELD_SIZE
                copyGrid[x, y] = 0
                if (findAllSolutions(copyGrid).size == 1) {
                    grid[x, y] = 0
                    fullCells.remove(i)
                }
                attempts--
            }

            return grid
        }

        private fun fillGrid(grid: Array2D, startPosition: Int = 0): Boolean {
            for (i in startPosition until FIELD_SIZE * FIELD_SIZE) {
                val x = i % 9
                val y = i / 9

                if (grid[x, y] == 0) {
                    val column = grid[x].getAvailableNumbers()
                    val row = grid[Unit, y].getAvailableNumbers()
                    val section = grid.getAvailableNumbersInSection(x, y)

                    val availableNumbers = column.intersect(row).intersect(section).shuffled()

                    for (number in availableNumbers) {
                        grid[x, y] = number
                        if (checkGrid(grid)) {
                            return true
                        } else {
                            if (fillGrid(grid, i + 1))
                                return true
                        }
                    }
                    grid[x, y] = 0
                    return false
                }
            }
            return true
        }

        private fun findAllSolutions(grid: Array2D, foundDecision: Int = 0, startPosition: Int = 0): List<Array2D> {
            for (i in startPosition until FIELD_SIZE * FIELD_SIZE) {
                val x = i % 9
                val y = i / 9

                if (grid[x, y] == 0) {
                    val column = grid[x].getAvailableNumbers()
                    val row = grid[Unit, y].getAvailableNumbers()
                    val section = grid.getAvailableNumbersInSection(x, y)

                    val availableNumbers = column.intersect(row).intersect(section)

                    val decisions = mutableListOf<Array2D>()
                    for (number in availableNumbers) {
                        grid[x, y] = number
                        if (checkGrid(grid)) {
                            val copyGrid = grid.clone()
                            grid[x, y] = 0
                            return listOf(copyGrid)
                        } else {
                            decisions += findAllSolutions(grid, foundDecision, i + 1)
                        }
                    }
                    grid[x, y] = 0
                    return decisions
                }
            }
            return listOf()
        }

        private fun checkGrid(grid: Array2D): Boolean {
            for (cell in grid) {
                if (cell == 0) return false
            }
            return true
        }
    }
}

private fun Array2D.getAvailableNumbersInColumn(ind: Int): Set<Int> {
    val availableList = (1..9).toMutableSet()
    availableList.removeAll(this[ind].toSet())
    return availableList
}

private fun Array2D.getAvailableNumbersInSection(x: Int, y: Int): Set<Int> {
    val availableList = (1..9).toMutableSet()
    val i = y / 3
    val j = x / 3
    for (row in 0..2) {
        for (column in 0..2) {
            availableList.remove(this[j * 3 + column, i * 3 + row])
        }
    }
    return availableList
}

private fun IntArray.getAvailableNumbers(): Set<Int> {
    val availableList = (1..9).toMutableSet()
    availableList.removeAll(this.asIterable())
    return availableList
}

private fun Array2D.getDuplicatesIndexesInColumn(ind: Int): List<Int> {
    val column = this[ind].toList()
    return column.mapIndexedNotNull { index, value ->
        if (value > 0 && column.filterIndexed { ind, i -> ind != index && i == value }.isNotEmpty()) index else null
    }
}

private fun IntArray.getDuplicatesIndexes(): List<Int> {
    val list = this.toList()
    return list.mapIndexedNotNull { index, value ->
        if (value > 0 && list.filterIndexed { ind, i -> ind != index && i == value }.isNotEmpty()) index else null
    }
}

private fun Array2D.getDuplicatesIndexesInSection(sectionX: Int, sectionY: Int): List<Coordinate> {
    val list = (0..8).map { ind -> this[sectionX * 3 + ind % 3, sectionY * 3 + ind / 3] }

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