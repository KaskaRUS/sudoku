package space.zhdanov.game.sudoku

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.onyx.android.sdk.pen.TouchHelper
import com.onyx.zhdanov.game.sudoku.Tutorial
import space.zhdanov.game.sudoku.components.GameView
import space.zhdanov.game.sudoku.components.PenHandler
import space.zhdanov.game.sudoku.databinding.ActivityTutorialBinding
import space.zhdanov.game.sudoku.models.Field
import space.zhdanov.game.sudoku.models.Grid
import com.onyx.zhdanov.game.sudoku.tutorialFieldGrid
import space.zhdanov.game.sudoku.utils.STEP_EXTRA
import space.zhdanov.game.sudoku.utils.drawRendererContent
import space.zhdanov.game.sudoku.utils.drawShadow

class TutorialActivity : AppCompatActivity() {

    private lateinit var surfaceView: GameView
    private lateinit var tutorial: Tutorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val binding = ActivityTutorialBinding.inflate(layoutInflater)
        step = intent.getIntExtra(STEP_EXTRA, step)
        surfaceView = binding.surfaceView
        surfaceView.grid = Grid(tutorialFieldGrid.clone()) {
            tutorial.nextStep()
        }

        surfaceView.onReady = { penHandler: PenHandler, touchHelper: TouchHelper, field: Field ->
            tutorial = Tutorial(step, surfaceView, binding.dialog, touchHelper, penHandler) {
                startActivity(Intent(this, MenuActivity::class.java))
            }
        }

        surfaceView.onRender = { canvas: Canvas ->
            surfaceView.field.draw()
            drawRendererContent(surfaceView.field.bitmap, canvas)
            drawShadow(canvas)
        }

        setContentView(binding.root)
    }

    companion object {
        var step = 0
    }
}