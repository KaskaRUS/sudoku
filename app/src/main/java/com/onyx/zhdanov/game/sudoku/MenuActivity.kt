package com.onyx.zhdanov.game.sudoku

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.onyx.android.sdk.rx.RxManager
import com.onyx.zhdanov.game.sudoku.databinding.ActivityMenuBinding
import com.onyx.zhdanov.game.sudoku.utils.CONTINUE_EXTRA
import com.onyx.zhdanov.game.sudoku.utils.CONTINUE_PREFERENCES
import com.onyx.zhdanov.game.sudoku.utils.DIFFICULT_EXTRA
import com.onyx.zhdanov.game.sudoku.utils.GAME_PREFERENCES_FILE
import com.onyx.zhdanov.game.sudoku.utils.STEP_EXTRA

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxManager.Builder.initAppContext(this.application)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        binding.isLoading.visibility = View.INVISIBLE

        val preferences = getSharedPreferences(GAME_PREFERENCES_FILE, MODE_PRIVATE)
        val continueGame = preferences.getBoolean(CONTINUE_PREFERENCES, false)

        binding.resume.visibility = if (continueGame) VISIBLE else GONE
        binding.resume.setOnClickListener {
            binding.isLoading.visibility = VISIBLE
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(CONTINUE_EXTRA, true)
            startActivity(intent)
        }

        val startGame: (View) -> Unit = {
            binding.isLoading.visibility = VISIBLE
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(DIFFICULT_EXTRA, it.tag.toString().toInt())
            startActivity(intent)
        }

        binding.easy.setOnClickListener(startGame)
        binding.medium.setOnClickListener(startGame)
        binding.hard.setOnClickListener(startGame)
        binding.tutorial.setOnClickListener {
            binding.isLoading.visibility = VISIBLE
            val intent = Intent(this, TutorialActivity::class.java)
            intent.putExtra(STEP_EXTRA, 0)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.isLoading.visibility = View.INVISIBLE
    }
}