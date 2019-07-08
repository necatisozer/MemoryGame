package com.necatisozer.memorygame.ui.game.level

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.necatisozer.memorygame.core.SingleLiveEvent
import com.necatisozer.memorygame.data.GameRepository
import com.necatisozer.memorygame.data.entity.Rules
import com.necatisozer.memorygame.ui.base.BaseViewModel
import com.necatisozer.memorygame.util.Card
import com.necatisozer.memorygame.util.GameEngine
import com.necatisozer.memorygame.util.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LevelViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameEngine: GameEngine
) : BaseViewModel() {
    private val cardsLiveData = MutableLiveData<List<Card>>()
    fun cardsLiveData(): LiveData<List<Card>> = cardsLiveData

    private val addScoreEvent = SingleLiveEvent<Int>()
    fun addScoreEvent(): LiveData<Int> = addScoreEvent

    private val proceedToNextLevelEvent = SingleLiveEvent<Int>()
    fun proceedToNextLevelEvent(): SingleLiveEvent<Int> = proceedToNextLevelEvent

    private val gameOverEvent = SingleLiveEvent<Void>()
    fun gameOverEvent(): SingleLiveEvent<Void> = gameOverEvent

    private lateinit var rules: Rules
    private var level: Int = 0

    init {
        initGame()
    }

    private fun initGame() {
        viewModelScope.launch {
            runCatching {
                if (::rules.isInitialized.not()) rules = gameRepository.getGameRules()

                val numberOfCards = rules.levels[level].numberOfBoxes
                val maxCardId = rules.maxCardId
                val cards = gameEngine.startGame(numberOfCards, maxCardId)

                cardsLiveData.value = cards
                proceedToNextLevelEvent.value = numberOfCards
            }.onFailure { ::handleFailure }
        }
    }

    fun onCardClick(index: Int) {
        val state = gameEngine.move(index)
        cardsLiveData.value = state.cards

        when (state) {
            is GameState.FlipBack -> flipBack()
            is GameState.Match -> addScoreEvent.value = rules.levels[level].pointsPerMatch
        }

        if (gameEngine.isOver()) {
            if (level < rules.levels.size - 1) {
                proceedToNextLevel()
            } else {
                gameOverEvent.call()
            }
        }
    }

    private fun flipBack() {
        viewModelScope.launch {
            delay(1000)
            val state = gameEngine.flipBack()
            cardsLiveData.value = state.cards
        }
    }

    private fun proceedToNextLevel() {
        level++
        initGame()
    }
}