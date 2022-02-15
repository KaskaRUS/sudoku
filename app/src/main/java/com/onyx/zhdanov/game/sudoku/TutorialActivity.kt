package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityTutorialBinding
import com.onyx.zhdanov.game.sudoku.models.Field
import com.onyx.zhdanov.game.sudoku.models.Grid
import com.onyx.zhdanov.game.sudoku.utils.plus

class TutorialActivity : AppCompatActivity() {

    lateinit var surfaceView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val binding = ActivityTutorialBinding.inflate(layoutInflater)
        surfaceView = binding.surfaceView
        surfaceView.grid = Grid(tutorialFieldGrid()) {}

        setContentView(binding.root)

    }

    override fun onBackPressed() {
        gotoFinishActivity("Pause")
    }

    private fun gotoFinishActivity(title: String) {
        val intent = Intent(this, FinishGameActivity::class.java)
        intent.putExtra("title", title)
        background = surfaceView.getBitmap()
        startActivity(intent)
    }

    private fun drawShadow(canvas: Canvas, field: Field) {
        val centerSection = field.getCellRect(3, 3) + field.getCellRect(5, 5)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutRect(centerSection)
        }
        canvas.drawColor(Color.GRAY, PorterDuff.Mode.DARKEN)
    }

    companion object {
        var background: Bitmap? = null
        private var difficult = 1
        private var tutorial = false
    }
}