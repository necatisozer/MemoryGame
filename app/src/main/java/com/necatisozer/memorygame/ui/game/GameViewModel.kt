package com.necatisozer.memorygame.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.necatisozer.memorygame.core.SingleLiveEvent
import com.necatisozer.memorygame.data.GameRepository
import com.necatisozer.memorygame.data.UserRepository
import com.necatisozer.memorygame.data.entity.Rules
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository
) : BaseViewModel() {
    private val scoreLiveData = MutableLiveData<Int>()
    fun scoreLiveData(): LiveData<Int> = scoreLiveData

    private val elapsedTimeLiveData = MutableLiveData<Duration>()
    fun elapsedTimeLiveData(): LiveData<Duration> = elapsedTimeLiveData

    private val gameOverEvent = SingleLiveEvent<GameResult>()
    fun gameOverEvent(): SingleLiveEvent<GameResult> = gameOverEvent

    private lateinit var rules: Rules
    private lateinit var user: User
    private lateinit var timerJob: Job

    init {
        viewModelScope.launch {
            runCatching {
                rules = gameRepository.getGameRules()
                user = userRepository.getUser()
                startGame()
            }.onFailure { ::handleFailure }
        }
    }

    private fun startGame() {
        scoreLiveData.value = 0

        timerJob = viewModelScope.launch {
            var elapsedTime = Duration.ofSeconds(rules.duration.toLong())
            elapsedTimeLiveData.value = elapsedTime

            do {
                delay(1000)
                elapsedTime = elapsedTime.minusSeconds(1)
                elapsedTimeLiveData.value = elapsedTime
            } while (elapsedTime.isZero.not())

            gameOver(success = false)
        }
    }

    fun addScore(score: Int) {
        scoreLiveData.value = scoreLiveData.value?.plus(score)
    }

    fun gameOver(success: Boolean) {
        val elapsedSeconds = elapsedTimeLiveData.value?.seconds?.toInt() ?: 0
        addScore(elapsedSeconds * rules.extraPointsPerSecond)

        val score = scoreLiveData.value ?: 0
        val highestScore = if (score > user.highestScore) score else user.highestScore
        gameOverEvent.value = GameResult(success, score, highestScore)

        updateUser()
    }

    private fun updateUser() {
        viewModelScope.launch {
            val score = scoreLiveData.value ?: 0
            userRepository.addNewScore(score)
        }
    }

}

data class GameResult(val success: Boolean, val score: Int, val highestScore: Int)