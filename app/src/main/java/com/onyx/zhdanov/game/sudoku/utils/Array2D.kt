package com.onyx.zhdanov.game.sudoku.utils

import java.lang.StringBuilder

class Array2D(val columns: Int, val rows: Int, constructor: (x: Int, y: Int) -> Int): Cloneable, Iterable<Int> {

    constructor(columns: Int, rows: Int, initial: Int): this(columns, rows, { _, _ -> initial })

    private val data: IntArray = IntArray(columns * rows) {
        val x = it % columns
        val y = it / columns
        constructor(x, y)
    }

    operator fun get(x: Int, y: Int): Int {
        return data[y * columns + x]
    }

    operator fun get(x: Int): IntArray {
        return data.filterIndexed { index, _ -> index % columns == x }.toIntArray()
    }

    operator fun get(x: Unit, y: Int): IntArray {
        return data.copyOfRange(y * columns, y * columns + columns)
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[y * columns + x] = value
    }

    fun copyInto(grid: Array2D) {
        data.copyInto(grid.data)
    }

    public override fun clone(): Array2D {
        return Array2D(columns, rows, 0).apply {
            this@Array2D.data.copyInto(this.data)
        }
    }

    override fun iterator(): Iterator<Int> =
        data.iterator()


    override fun toString(): String {
        val text = StringBuilder()
        text.append("Array2D:")
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                text.append("${this[j, i]} ")
            }
            text.append('\n')
        }
        return text.toString()
    }
}

fun array2dOf(columns: Int, rows: Int, constructor: () -> IntArray): Array2D {
    val data = constructor()
    return Array2D(columns, rows) { x, y ->
        data[x + y * columns]
    }
}