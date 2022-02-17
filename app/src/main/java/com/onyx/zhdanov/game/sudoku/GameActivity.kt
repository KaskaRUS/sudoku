package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.onyx.zhdanov.game.sudoku.components.GameView
import com.onyx.zhdanov.game.sudoku.databinding.ActivityGameBinding
import com.onyx.zhdanov.game.sudoku.models.Grid

class GameActivity : AppCompatActivity() {

    private lateinit var surfaceView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        difficult = intent.getIntExtra("difficult", difficult)

        val grid = Grid(difficult) {
            gotoFinishActivity("Congratulation!")
        }
        val binding = ActivityGameBinding.inflate(layoutInflater)
        surfaceView = binding.surfaceView
        surfaceView.grid = grid

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        gotoFinishActivity("Pause")
    }

    private fun gotoFinishActivity(title: String) {
        val intent = Intent(this, FinishGameActivity::class.java)
        intent.putExtra("title", title)
        surfaceView.render()
        background = surfaceView.getBitmap()
        startActivity(intent)
    }

    companion object {
        var background: Bitmap? = null
        private var difficult = 1
    }
}