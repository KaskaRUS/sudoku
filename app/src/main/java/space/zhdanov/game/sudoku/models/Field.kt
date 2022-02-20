package space.zhdanov.game.sudoku.models

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

class Field(width: Int, height: Int, val grid: Grid) {

    var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    private val paint = Paint().apply {
        color = Color.BLACK
    }

    private val mistakeRectPaint = Paint().apply {
        color = Color.LTGRAY
    }

    private var drawableConfiguration: DrawableConfiguration = getDrawableConfiguration(width, height)
    private val textBound = Rect()

    fun draw() {
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        for (mistake in grid.mistakes) {
            canvas.drawRect(getCellRect(mistake.x, mistake.y), mistakeRectPaint)
        }

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
                if (grid.grid[j, i] > 0) {
                    val text = grid.grid[j, i].toString()
                    val textPaint = if (grid.isStartedCell(j, i)) {
                        drawableConfiguration.boldDigitPaint
                    } else {
                        drawableConfiguration.digitPaint
                    }
                    textPaint.getTextBounds(text, 0, 1, textBound)
                    val x: Float =
                        drawableConfiguration.paddingX + (j + 0.5f) * drawableConfiguration.cellSize - textBound.exactCenterX()
                    val y: Float =
                        drawableConfiguration.paddingY + (i + 0.5f) * drawableConfiguration.cellSize - textBound.exactCenterY()
                    canvas.drawText(text, x, y, textPaint)
                }
            }
        }
    }

    fun sizeChange(width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawableConfiguration = getDrawableConfiguration(width, height)
    }

    fun getCellRect(refreshRect: RectF): RectF {
        val (j, i) = getCellCoordinates(refreshRect.centerX(), refreshRect.centerY())
        return getCellRect(j, i)
    }

    fun getCellRect(j: Int, i: Int): RectF {
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

    fun getRectOfMistakes(): List<RectF> =
        grid.mistakes.map { getCellRect(it.x, it.y) }

    private fun getDrawableConfiguration(width: Int, height: Int): DrawableConfiguration {
        val cellSize = (min(width, height) - 2 * MIN_PADDING) / FIELD_SIZE.toFloat()

        return DrawableConfiguration(
            width = width,
            height = height,
            digitPaint = Paint().apply {
                textSize = cellSize * 0.8f
            },
            boldDigitPaint = Paint().apply {
                textSize = cellSize * 0.6f
                isFakeBoldText = true
            },
            cellSize = cellSize,
            paddingX = (width - cellSize * FIELD_SIZE) / 2,
            paddingY = (height - cellSize * FIELD_SIZE) / 2
        )
    }

    companion object {
        const val MIN_PADDING = 20
        const val BOLD_LINE_WEIGHT = 6f
        const val STROKE_LINE_WEIGHT = 2f
        const val FIELD_SIZE = 9
    }
}

data class DrawableConfiguration(
    val digitPaint: Paint,
    val boldDigitPaint: Paint,
    val cellSize: Float,
    val paddingX: Float,
    val paddingY: Float,
    val width: Int,
    val height: Int,
)