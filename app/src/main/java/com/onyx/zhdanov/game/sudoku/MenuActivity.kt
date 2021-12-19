package com.onyx.zhdanov.game.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.onyx.android.sdk.rx.RxManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxManager.Builder.initAppContext(this.application)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val startButton = binding.start

        startButton.setOnClickListener {
            Log.d("menu", "start")
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }
    }


}