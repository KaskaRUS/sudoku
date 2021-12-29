package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    lateinit var surfaceView: MySurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val binding = ActivityFullscreenBinding.inflate(layoutInflater)
        surfaceView = binding.surfaceView
        difficult = intent.getIntExtra("difficult", difficult)
        surfaceView.grid = Grid(difficult)

        setContentView(binding.root)

        surfaceView.onSuccess = {
            gotoFinishActivity("Congratulation!")
        }
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

    companion object {
        var background: Bitmap? = null
        private var difficult = 1
    }
}