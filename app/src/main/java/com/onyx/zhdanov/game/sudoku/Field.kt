package com.onyx.zhdanov.game.sudoku

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import java.lang.StringBuilder
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.random.Random

class Field(width: Int, height: Int) {

    var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    private val paint = Paint().apply {
        color = Color.BLACK
    }

    private var drawableConfiguration: DrawableConfiguration = getDrawableConfiguration(width, height)
    private val grid = Grid()
    private val textBound = Rect()

    fun draw(refreshRect: RectF = RectF()) {
        val canvas = Canvas(bitmap)
        if (!refreshRect.isEmpty) canvas.clipRect(refreshRect)
        canvas.drawColor(Color.WHITE)

        // horizontal lines
        for (i in 0..FIELD_SIZE) {
            paint.strokeWidth = if (i % 3 == 0) BOLD_LINE_WEIGHT else STROKE_LINE_WEIGHT
            val sX: Float = drawableConfiguration.paddingX
            val sY: Float = drawableConfiguration.paddingY + i * drawableConfiguration.cellSize
            val eX: Float = drawableConfiguration.paddingX + FIELD_SIZE * drawableConfiguration.cellSize
            val eY: Float = drawableConfiguration.paddingY + i * drawableConfiguration.cellSize
            canvas.drawLine(sX, sY, eX, eY, paint)
        }

        // vertical lines
        for (i in 0..FIELD_SIZE) {
            paint.strokeWidth = if (i % 3 == 0) BOLD_LINE_WEIGHT else STROKE_LINE_WEIGHT
            val sX: Float = drawableConfiguration.paddingX + i * drawableConfiguration.cellSize
            val sY: Float = drawableConfiguration.paddingY
            val eX: Float = drawableConfiguration.paddingX + i * drawableConfiguration.cellSize
            val eY: Float = drawableConfiguration.paddingY + FIELD_SIZE * drawableConfiguration.cellSize
            canvas.drawLine(sX, sY, eX, eY, paint)
        }

        for (i in 0 until FIELD_SIZE) {
            for (j in 0 until FIELD_SIZE) {
                if (grid[i][j] > 0) {
                    val text = grid[i][j].toString()
                    drawableConfiguration.digitPaint.getTextBounds(text, 0, 1, textBound)
                    val x: Float =
                        drawableConfiguration.paddingX + (j + 0.5f) * drawableConfiguration.cellSize - textBound.exactCenterX()
                    val y: Float =
                        drawableConfiguration.paddingY + (i + 0.5f) * drawableConfiguration.cellSize - textBound.exactCenterY()
                    canvas.drawText(text, x, y, drawableConfiguration.digitPaint)
                }
            }
        }
    }

    fun changeNumber(x: Float, y: Float, newValue: Int) {
        val (j, i) = getCellCoordinates(x, y)
        grid[i][j] = newValue
        draw(getCellRect(j, i))
    }

    fun sizeChange(width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawableConfiguration = getDrawableConfiguration(width, height)
    }

    fun getCellRect(refreshRect: RectF): RectF {
        val (j, i) = getCellCoordinates(refreshRect.centerX(), refreshRect.centerY())
        Log.i("field", "x: $j, y: $i")
        return getCellRect(j, i)
    }

    private fun getCellRect(j: Int, i: Int): RectF {
        return RectF(
            drawableConfiguration.paddingX + j * drawableConfiguration.cellSize,
            drawableConfiguration.paddingY + i * drawableConfiguration.cellSize,
            drawableConfiguration.paddingX + (j + 1) * drawableConfiguration.cellSize,
            drawableConfiguration.paddingY + (i + 1) * drawableConfiguration.cellSize,
        )
    }

    fun getCellCoordinates(x: Float, y: Float) = Pair(
        floor((x - drawableConfiguration.paddingX) / drawableConfiguration.cellSize.toDouble()).roundToInt(),
        floor((y - drawableConfiguration.paddingY) / drawableConfiguration.cellSize.toDouble()).roundToInt(),
    )

    private fun getDrawableConfiguration(width: Int, height: Int): DrawableConfiguration {
        Log.i("field", "width: $width, height: $height")
        return if (width < height) {
            val cellSize = (width - 2 * MIN_PADDING) / FIELD_SIZE.toFloat()
            Log.i("field", "cellSize: $cellSize")

            DrawableConfiguration(
                digitPaint = Paint().apply {
                    textSize = cellSize * 0.7f
                },
                cellSize = cellSize,
                paddingX = MIN_PADDING.toFloat(),
                paddingY = (height - cellSize * FIELD_SIZE) / 2
            )
        } else {
            val cellSize = (height - 2 * MIN_PADDING) / FIELD_SIZE.toFloat()
            Log.i("field", "cellSize: $cellSize")
            DrawableConfiguration(
                digitPaint = Paint().apply {
                    textSize = cellSize * 0.7f
                },
                cellSize = cellSize,
                paddingY = MIN_PADDING.toFloat(),
                paddingX = (width - cellSize * FIELD_SIZE) / 2
            )
        }
    }

    companion object {
        const val MIN_PADDING = 20
        const val BOLD_LINE_WEIGHT = 6f
        const val STROKE_LINE_WEIGHT = 2f
        const val FIELD_SIZE = 9
        const val EMPTY_CELL_VALUE = 0
    }
}

data class DrawableConfiguration(
    val digitPaint: Paint,
    val cellSize: Float,
    val paddingX: Float,
    val paddingY: Float,
)