package com.necatisozer.memorygame.ui.game

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.databinding.ActivityGameBinding
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseActivity
import org.threeten.bp.Duration
import splitties.arch.lifecycle.observeNotNull

class GameActivity : BaseActivity() {
    private lateinit var binding: ActivityGameBinding
    private val viewModel by viewModels { injector.gameViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        observeViewModel()
    }

    fun observeViewModel() {
        observeNotNull(viewModel.elapsedTimeLiveData()) { updateElapsedTime(it) }
        observeNotNull(viewModel.scoreLiveData()) { updateScore(it) }
        viewModel.gameOverEvent().observe(this, Observer { showResults(it) })
    }

    private fun updateElapsedTime(elapsedTime: Duration) {
        val minutes = elapsedTime.toMinutes()
        val seconds = elapsedTime.minusMinutes(minutes).seconds
        binding.layoutGameToolbar.textViewTime.text = getString(R.string.game_time, minutes, seconds)
    }

    private fun updateScore(score: Int) {
        binding.layoutGameToolbar.textViewScore.text = getString(R.string.game_score, score)
    }

    private fun showResults(gameResult: GameResult) {
        val titleRes = if (gameResult.success) R.string.game_result_title_success else R.string.game_result_title_fail
        val message = getString(R.string.game_result_message, gameResult.score, gameResult.highestScore)

        MaterialDialog(this).show {
            title(titleRes)
            message(text = message)
            positiveButton(android.R.string.ok) { returnHighestScore(gameResult.highestScore) }
            cancelable(false)
        }
    }

    private fun returnHighestScore(highestScore: Int) {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra("highest_score", highestScore) })
        finish()
    }
}