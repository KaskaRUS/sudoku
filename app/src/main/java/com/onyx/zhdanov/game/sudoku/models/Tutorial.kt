package com.onyx.zhdanov.game.sudoku

import android.support.constraint.ConstraintLayout
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.zhdanov.game.sudoku.components.GameView
import com.onyx.zhdanov.game.sudoku.components.PenHandler
import com.onyx.zhdanov.game.sudoku.databinding.DialogBinding
import com.onyx.zhdanov.game.sudoku.models.Field
import com.onyx.zhdanov.game.sudoku.utils.drawRendererContent
import com.onyx.zhdanov.game.sudoku.utils.drawShadow
import com.onyx.zhdanov.game.sudoku.utils.drawShadowWithoutRect
import com.onyx.zhdanov.game.sudoku.utils.plus

/*
1. Show rules of games:
    a. You have to fill all cells in field
    b. In sections have to be only digit 1 to 9 unique
    c. In columns have to be only digit 1 to 9 unique
    d. In rows have to be only digit 1 to 9 unique
2. Write digit
3. Add mistake
4. Remove digit
5. Back to menu
6. Complete field
 */

fun tutorialFieldGrid() = arrayOf(
    intArrayOf(1, 0, 3, 4, 5, 6, 7, 8, 9),
    intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
    intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
    intArrayOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
    intArrayOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
    intArrayOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
    intArrayOf(3, 4, 5, 6, 7, 8, 9, 0, 2),
    intArrayOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
    intArrayOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
)

class Tutorial(
    var step: Int,
    private val gameView: GameView,
    private val dialogBinding: DialogBinding,
    private val touchHelper: TouchHelper,
    private val field: Field,
    private val penHandler: PenHandler,
    private val completeStep: (step: Int) -> Unit
) {
    private val steps = mapOf(
        1 to {
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Welcome! The goal of this game is to fill all of the cells with numbers."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadow(it)
            }
            gameView.render()

            completeStep(1)
        },
        2 to {
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Each 3×3 box can only contain each number from 1 to 9 once."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            val centerSection = field.getCellRect(3, 3) + field.getCellRect(5, 5)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            completeStep(2)
        },
        3 to {
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Each line can only contain each number from 1 to 9 once."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            val centerSection = field.getCellRect(0, 2) + field.getCellRect(8, 2)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            completeStep(3)
        },
        4 to {
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Each column can only contain each number from 1 to 9 once."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            params.leftMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            val centerSection = field.getCellRect(6, 0) + field.getCellRect(6, 8)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            completeStep(4)
        },
        5 to {
            dialogBinding.buttonNext.isEnabled = false
            dialogBinding.textDialog.text = "Try write digit 1 in white cell."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            params.leftMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(true)
                .setRawDrawingRenderEnabled(true)

            val centerSection = field.getCellRect(7, 6)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            penHandler.onRecognizeDigit = { x: Int, y: Int, digit: Int ->
                if (digit == 1 && x == 7 && y == 6) {
                    field.grid.changeCell(x, y, digit)
                    dialogBinding.textDialog.text = "Great! You can see that instead of your handwritten number, the printed same number drawn. Lets go next."
                    completeStep(5)
                } else {
                    dialogBinding.textDialog.text = "Try write digit 1 in white cell, again..."
                }
                true
            }
        },
        6 to {
            dialogBinding.buttonNext.isEnabled = false
            dialogBinding.textDialog.text = "Try cross the digit by cross which is like 'X'"
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            params.leftMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(true)
                .setRawDrawingRenderEnabled(true)

            val centerSection = field.getCellRect(7, 6)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            penHandler.onRecognizeDigit = { x: Int, y: Int, digit: Int ->
                if (digit == 0 && x == 7 && y == 6) {
                    field.grid.changeCell(x, y, digit)
                    dialogBinding.textDialog.text = "Cool! So you can correct mistakes."

                    completeStep(6)
                } else {
                    dialogBinding.textDialog.text = "Try cross the digit by cross which is like 'X', again..."
                }
                true
            }
        },
        7 to {
            dialogBinding.buttonNext.isEnabled = false
            dialogBinding.textDialog.text = "Try write digit 4 in white cell."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            params.leftMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(true)
                .setRawDrawingRenderEnabled(true)

            val centerSection = field.getCellRect(7, 6)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()

            penHandler.onRecognizeDigit = { x: Int, y: Int, digit: Int ->
                if (digit == 4 && x == 7 && y == 6) {
                    field.grid.changeCell(x, y, digit)
                    nextStep()
                } else {
                    dialogBinding.textDialog.text = "Try write digit 4 in white cell, again..."
                }
                true
            }
        },
        8 to {
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Don't worry! If you made mistake, all mistakes will be marked on field."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            params.leftMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            val centerSection = field.getCellRect(0, 5) + field.getCellRect(8, 7)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()
        },
        9 to {
            dialogBinding.buttonNext.isEnabled = false
            dialogBinding.textDialog.text = "Pay attention to that your digit is bigger than other. You can only change big digits."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            val centerSection = field.getCellRect(0, 5) + field.getCellRect(8, 7)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadowWithoutRect(it, centerSection)
            }
            gameView.render()
            completeStep(7)
        },
        10 to {
            dialogBinding.buttonNext.isEnabled = false
            dialogBinding.textDialog.text = "Now complete the field. Lets go!"
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = 32
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)


            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
                drawShadow(it)
            }
            gameView.render()
            completeStep(8)
        },
        11 to {
            dialogBinding.root.visibility = INVISIBLE

            touchHelper.setRawDrawingEnabled(true)
                .setRawDrawingRenderEnabled(true)

            penHandler.onRecognizeDigit = { x: Int, y: Int, digit: Int ->
                field.grid.changeCell(x, y, digit)
            }

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
            }
            gameView.render()
        },
        12 to {
            dialogBinding.root.visibility = VISIBLE
            dialogBinding.buttonNext.isEnabled = true
            dialogBinding.textDialog.text = "Congratulation! You just complete the tutorial. Now you can solve real sudoku."
            val params = dialogBinding.view.layoutParams as ConstraintLayout.LayoutParams
            params.leftMargin = gameView.width / 2 - dialogBinding.view.width / 2
            params.topMargin = gameView.height / 2 - dialogBinding.view.height / 2
            dialogBinding.view.layoutParams = params

            touchHelper.setRawDrawingEnabled(false)
                .setRawDrawingRenderEnabled(false)

            gameView.onRender = {
                field.draw()
                drawRendererContent(field.bitmap, it)
            }
            gameView.render()
            completeStep(12)
        },
        LAST_STEP to {
            completeStep(LAST_STEP)
        }
    )

    init {
        steps[step]?.let { it() }
    }

    fun nextStep() {
        step++
        steps[step]?.let { it() }
    }

    companion object {
        const val LAST_STEP = 13
    }
}