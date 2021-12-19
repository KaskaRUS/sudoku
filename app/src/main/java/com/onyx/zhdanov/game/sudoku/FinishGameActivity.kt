package com.onyx.zhdanov.game.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.onyx.zhdanov.game.sudoku.databinding.ActivityFinishGameBinding

class FinishGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishGameBinding
    private lateinit var titleText: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFinishGameBinding.inflate(layoutInflater)
        titleText = binding.finishTitle
        val finishButton = binding.finish
        val retryButton = binding.retry

        setContentView(binding.root)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        titleText.text = intent.getStringExtra("title")

        FullscreenActivity.background?.let { background ->
            binding.root.background = BitmapDrawable(resources, background)
        }

        finishButton.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        retryButton.setOnClickListener {
            startActivity(Intent(this, FullscreenActivity::class.java))
        }
    }
}