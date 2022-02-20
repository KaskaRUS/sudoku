package space.zhdanov.game.sudoku

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import io.sentry.Sentry
import io.sentry.SentryLevel
import space.zhdanov.game.sudoku.components.GameView
import space.zhdanov.game.sudoku.databinding.ActivityGameBinding
import space.zhdanov.game.sudoku.models.Field.Companion.FIELD_SIZE
import space.zhdanov.game.sudoku.models.Grid
import space.zhdanov.game.sudoku.utils.Array2D
import space.zhdanov.game.sudoku.utils.CONTINUE_EXTRA
import space.zhdanov.game.sudoku.utils.CONTINUE_PREFERENCES
import space.zhdanov.game.sudoku.utils.DIFFICULT_EXTRA
import space.zhdanov.game.sudoku.utils.GAME_PREFERENCES_FILE
import space.zhdanov.game.sudoku.utils.PLAYER_SOLUTION_PREFERENCES
import space.zhdanov.game.sudoku.utils.STARTED_GRID_PREFERENCES
import space.zhdanov.game.sudoku.utils.TITLE_EXTRA
import space.zhdanov.game.sudoku.utils.array2dFromStrings

class GameActivity : AppCompatActivity() {

    private val LOG_TAG = GameActivity::class.qualifiedName

    private lateinit var surfaceView: GameView
    private lateinit var grid: Grid
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences(GAME_PREFERENCES_FILE, MODE_PRIVATE)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        difficult = intent.getIntExtra(DIFFICULT_EXTRA, difficult)
        val continueGame = intent.getBooleanExtra(CONTINUE_EXTRA, false)

        if (continueGame && preferences.contains(CONTINUE_PREFERENCES)) {
            try {
                difficult = preferences.getInt(DIFFICULT_EXTRA, difficult)
                val startedGrid: Array2D =
                    array2dFromStrings(FIELD_SIZE, FIELD_SIZE, preferences.getString(STARTED_GRID_PREFERENCES, "")!!)
                val playerSolution: Array2D =
                    array2dFromStrings(FIELD_SIZE, FIELD_SIZE, preferences.getString(PLAYER_SOLUTION_PREFERENCES, "")!!)

                grid = Grid(startedGrid) {
                    gotoFinishActivity("Congratulation!")
                }
                grid.loadUserSolution(playerSolution)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Fail of loading state", e)
                Sentry.captureMessage("Fail of loading state", SentryLevel.ERROR)
                Sentry.captureException(e)
                preferences.edit().putBoolean(CONTINUE_PREFERENCES, false).apply()
                gotoMenuActivity()
                return
            }
        } else {
            grid = Grid(difficult) {
                gotoFinishActivity("Congratulation!")
            }
        }
        val binding = ActivityGameBinding.inflate(layoutInflater)
        surfaceView = binding.surfaceView
        surfaceView.grid = grid

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        gotoFinishActivity("Pause")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (grid.checkSuccess()) {
            preferences.edit()
                .putBoolean(CONTINUE_PREFERENCES, false)
                .apply()
        } else {
            val startedGrid = grid.firstRepresentation.joinToString(",")
            val playerSolution = grid.grid.joinToString(",")
            preferences.edit()
                .putBoolean(CONTINUE_PREFERENCES, true)
                .putString(STARTED_GRID_PREFERENCES, startedGrid)
                .putString(PLAYER_SOLUTION_PREFERENCES, playerSolution)
                .apply()
        }
    }

    private fun gotoFinishActivity(title: String) {
        val intent = Intent(this, FinishGameActivity::class.java)
        intent.putExtra(TITLE_EXTRA, title)
        surfaceView.render()
        background = surfaceView.getBitmap()
        startActivity(intent)
    }

    private fun gotoMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    companion object {
        var background: Bitmap? = null
        private var difficult = 1
    }
}