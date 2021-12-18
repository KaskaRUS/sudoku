package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.onyx.android.sdk.rx.RxManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    lateinit var surfaceView: MySurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxManager.Builder.initAppContext(this.application)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val binding = ActivityFullscreenBinding.inflate(layoutInflater)
        surfaceView = binding.surfaceView
        setContentView(binding.root)

        binding.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

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
    }
}