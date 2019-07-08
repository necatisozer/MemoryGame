package com.necatisozer.memorygame.util

import javax.inject.Inject

class GameEngine @Inject constructor() {
    private val cards = mutableListOf<Card>()
    private lateinit var state: GameState

    fun startGame(numberOfCards: Int, maxCardId: Int): List<Card> {
        if (numberOfCards % 2 == 1) throw IllegalArgumentException("The number of cards should be even")
        cards.clear()
        createCards(numberOfCards, maxCardId)
        state = GameState.Start(cards)
        return cards
    }

    private fun createCards(numberOfCards: Int, maxCardId: Int) {
        repeat(numberOfCards / 2) {
            val cardId = (0..maxCardId).random()

            repeat(2) {
                cards.add(Card(cardId, Card.State.BACK))
            }
        }

        cards.shuffle()
    }

    fun move(index: Int): GameState {
        if (::state.isInitialized.not()) error("The game has not started yet. Start the game with startGame function")

        when (state) {
            is GameState.Start -> {
                cards[index].flip()
                state = GameState.NextMoveWaiting(cards, index)
            }
            is GameState.Match -> {
                cards[index].flip()
                state = GameState.NextMoveWaiting(cards, index)
            }
            is GameState.NextMoveWaiting -> {
                cards[index].flip()

                val flippedIndex = (state as GameState.NextMoveWaiting).flippedIndex

                if (cards[index].id == cards[flippedIndex].id) {
                    state = GameState.Match(cards)
                } else {
                    state = GameState.FlipBack(cards, flippedIndex, index)
                }
            }
        }

        return state
    }

    fun flipBack(): GameState {
        if (state is GameState.FlipBack) {
            val flipBackIndex1 = (state as GameState.FlipBack).flipBackIndex1
            val flipBackIndex2 = (state as GameState.FlipBack).flipBackIndex2
            cards[flipBackIndex1].flip()
            cards[flipBackIndex2].flip()
            state = GameState.Start(cards)
        }

        return state
    }

    fun isOver(): Boolean {
        cards.forEach {
            if (it.state == Card.State.BACK) return false
        }

        return true
    }
}

data class Card(val id: Int, var state: State) {
    fun flip() {
        state = if (state == State.FRONT) State.BACK else State.FRONT
    }

    enum class State { FRONT, BACK }
}

sealed class GameState(open val cards: List<Card>) {
    class Start(override val cards: List<Card>) : GameState(cards)
    class NextMoveWaiting(override val cards: List<Card>, val flippedIndex: Int) : GameState(cards)
    class Match(override val cards: List<Card>) : GameState(cards)
    class FlipBack(override val cards: List<Card>, val flipBackIndex1: Int, val flipBackIndex2: Int) : GameState(cards)
}
