package com.onyx.zhdanov.game.sudoku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.onyx.android.sdk.rx.RxManager
import com.onyx.android.game.sudoku.databinding.ActivityFullscreenBinding

class FullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxManager.Builder.initAppContext(this.application)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}