package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.onyx.android.sdk.rx.RxManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxManager.Builder.initAppContext(this.application)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        binding.isLoading.visibility = View.INVISIBLE


        val startGame: (View) -> Unit = {
            binding.isLoading.visibility = View.VISIBLE
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("difficult", it.tag.toString().toInt())
            startActivity(intent)
        }

        binding.easy.setOnClickListener(startGame)
        binding.medium.setOnClickListener(startGame)
        binding.hard.setOnClickListener(startGame)
        binding.tutorial.setOnClickListener {
            binding.isLoading.visibility = View.VISIBLE
            val intent = Intent(this, TutorialActivity::class.java)
            intent.putExtra("step", 0)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.isLoading.visibility = View.INVISIBLE
    }
}