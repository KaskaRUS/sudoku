package com.onyx.zhdanov.game.sudoku

import android.graphics.PointF
import android.graphics.Rect
import android.support.constraint.ConstraintLayout
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.zhdanov.game.sudoku.components.GameView
import com.onyx.zhdanov.game.sudoku.components.PenHandler
import com.onyx.zhdanov.game.sudoku.databinding.DialogBinding
import com.onyx.zhdanov.game.sudoku.models.Field.Companion.FIELD_SIZE
import com.onyx.zhdanov.game.sudoku.utils.array2dOf
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent
import com.onyx.zhdanov.game.sudoku.utils.drawShadow
import com.onyx.zhdanov.game.sudoku.utils.drawShadowWithoutRect
import com.onyx.zhdanov.game.sudoku.utils.plus

val tutorialFieldGrid = array2dOf(FIELD_SIZE, FIELD_SIZE) {
    intArrayOf(
        1, 0, 3, 4, 5, 6, 7, 8, 9,
        4, 5, 6, 7, 8, 9, 1, 2, 3,
        7, 8, 9, 1, 2, 3, 4, 5, 6,
        2, 3, 4, 5, 6, 7, 8, 9, 1,
        5, 6, 7, 8, 9, 1, 2, 3, 4,
        8, 9, 1, 2, 3, 4, 5, 6, 7,
        3, 4, 5, 6, 7, 8, 9, 0, 2,
        6, 7, 8, 9, 1, 2, 3, 4, 5,
        9, 1, 2, 3, 4, 5, 6, 7, 8
    )
}

data class ScreenState(
    val buttonEnable: Boolean,
    val message: String,
    val alertPosition: PointF,
    val penIsEnable: Boolean,
    val shadow: Boolean,
    val focusRect: Rect? = null,
    val autoComplete: Boolean,
    val predict: ((x: Int, y: Int, digit: Int) -> Boolean)? = null,
    val alertVisible: Boolean = true
)

private val steps = listOf(
    ScreenState(
        buttonEnable = true,
        message = "Welcome! The goal of this game is to fill all of the cells with numbers.",
        alertPosition = PointF(0.5f, 0.05f),
        penIsEnable = false,
        shadow = true,
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = true,
        message = "Each 3×3 box can only contain each number from 1 to 9 once.",
        alertPosition = PointF(0.5f, 0.05f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(3, 3, 5, 5),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = true,
        message = "Each line can only contain each number from 1 to 9 once.",
        alertPosition = PointF(0.5f, 0.05f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(0, 2, 8, 2),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = true,
        message = "Each column can only contain each number from 1 to 9 once.",
        alertPosition = PointF(0.05f, 0.5f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(6, 0, 6, 8),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = false,
        message = "Try write digit 1 in white cell.",
        alertPosition = PointF(0.95f, 0.5f),
        penIsEnable = true,
        shadow = true,
        focusRect = Rect(7, 6, 7, 6),
        autoComplete = true,
        predict = { x: Int, y: Int, digit: Int ->
            digit == 1 && x == 7 && y == 6
        }
    ),
    ScreenState(
        buttonEnable = true,
        message = "Great! You can see that instead of your handwritten number, the printed same number drawn. Lets go next.",
        alertPosition = PointF(0.95f, 0.5f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(7, 6, 7, 6),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = false,
        message = "Try cross the digit by cross which is like 'X'",
        alertPosition = PointF(0.95f, 0.5f),
        penIsEnable = true,
        shadow = true,
        focusRect = Rect(7, 6, 7, 6),
        autoComplete = true,
        predict = { x: Int, y: Int, digit: Int ->
            digit == 0 && x == 7 && y == 6
        }
    ),
    ScreenState(
        buttonEnable = true,
        message = "Cool! So you can correct mistakes.",
        alertPosition = PointF(0.95f, 0.5f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(7, 6, 7, 6),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = false,
        message = "Try write digit 4 in white cell.",
        alertPosition = PointF(0.95f, 0.5f),
        penIsEnable = true,
        shadow = true,
        focusRect = Rect(7, 6, 7, 6),
        autoComplete = true,
        predict = { x: Int, y: Int, digit: Int ->
            digit == 4 && x == 7 && y == 6
        }
    ),
    ScreenState(
        buttonEnable = true,
        message = "Don't worry! If you made mistake, all mistakes will be marked on field.",
        alertPosition = PointF(0.5f, 0.3f),
        penIsEnable = false,
        shadow = true,
        focusRect = Rect(0, 5, 8, 7),
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = true,
        message = "Now complete the field. Lets go!",
        alertPosition = PointF(0.5f, 0.5f),
        penIsEnable = false,
        shadow = true,
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = false,
        message = "",
        alertPosition = PointF(0.5f, 0.5f),
        penIsEnable = true,
        shadow = false,
        autoComplete = false,
        alertVisible = false
    ),
    ScreenState(
        buttonEnable = true,
        message = "Congratulation! You just complete the tutorial. Now you can solve real sudoku.",
        alertPosition = PointF(0.5f, 0.5f),
        penIsEnable = false,
        shadow = true,
        autoComplete = false,
    ),
    ScreenState(
        buttonEnable = true,
        message = "",
        alertPosition = PointF(0.5f, 0.05f),
        penIsEnable = false,
        shadow = true,
        autoComplete = true,
        alertVisible = false
    ),
)

class Tutorial(
    var step: Int,
    private val gameView: GameView,
    private val dialogBinding: DialogBinding,
    private val touchHelper: TouchHelper,
    private var penHandler: PenHandler,
    private val completeStep: () -> Unit
) {
    init {
        updateScreen(step)

        gameView.onUpdate = {
            penHandler = it
            updateScreen(step)
        }

        dialogBinding.buttonNext.setOnClickListener {
            nextStep()
        }
    }

    fun nextStep() {
        updateScreen(++step)
        TutorialActivity.step = step
    }

    private fun updateScreen(step: Int) {
        val stepData = steps[step]

        dialogBinding.root.visibility = if (stepData.alertVisible) VISIBLE else INVISIBLE

        dialogBinding.buttonNext.isEnabled = stepData.buttonEnable
        dialogBinding.textDialog.text = stepData.message

        val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
        params.verticalBias = stepData.alertPosition.y
        params.horizontalBias = stepData.alertPosition.x
        dialogBinding.view.layoutParams = params

        penHandler.penEnable = stepData.penIsEnable
        touchHelper.setRawDrawingEnabled(stepData.penIsEnable)
            .setRawDrawingRenderEnabled(stepData.penIsEnable)

        val centerSection = stepData.focusRect
            ?.let { gameView.field.getCellRect(it.left, it.top) + gameView.field.getCellRect(it.right, it.bottom) }

        gameView.onRender = { canvas ->
            gameView.field.draw()
            drawRendererContent(gameView.field.bitmap, canvas)
            if (stepData.shadow) {
                if (centerSection !== null) {
                    drawShadowWithoutRect(canvas, centerSection)
                } else {
                    drawShadow(canvas)
                }
            }
        }
        gameView.render()

        if (stepData.predict == null) {
            penHandler.onRecognizeDigit = null
            if (stepData.autoComplete && !isFinishStep(step)) {
                nextStep()
            } else {
                completeStep(isFinishStep(step))
            }
        } else {
            penHandler.onRecognizeDigit = { x: Int, y: Int, digit: Int ->
                if (stepData.predict?.let { it(x, y, digit) }) {
                    gameView.field.grid.changeCell(x, y, digit)
                    if (stepData.autoComplete) {
                        nextStep()
                    } else {
                        completeStep(isFinishStep(step))
                    }
                    penHandler.penEnable = false
                }

                true
            }
        }
    }

    private fun isFinishStep(step: Int) = step == steps.size - 1

    private fun completeStep(last: Boolean) {
        if (last) {
            completeStep()
        } else {
            dialogBinding.buttonNext.isEnabled = true
        }
    }
}