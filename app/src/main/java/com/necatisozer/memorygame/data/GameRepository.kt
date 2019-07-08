package com.necatisozer.memorygame.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.necatisozer.memorygame.data.GameRepository.Companion.MAX_CARD_ID
import com.necatisozer.memorygame.data.dbo.GameRules
import com.necatisozer.memorygame.data.entity.Level
import com.necatisozer.memorygame.data.entity.Rules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor() {
    companion object {
        const val GAME_PATH = "game"
        const val RULES_PATH = "rules"
        const val MAX_CARD_ID = 1185
    }

    private val db = FirebaseFirestore.getInstance()

    suspend fun getGameRules(): Rules = withContext(Dispatchers.IO) {
        val rulesSnapshot = db.collection(GAME_PATH).document(RULES_PATH).get().await()
        val rules = rulesSnapshot.toObject<GameRules>()

        rules.asEntity
    }
}

private val GameRules?.asEntity
    get() = Rules(
        duration = this?.duration ?: 0,
        extraPointsPerSecond = this?.extraPointsPerSecond ?: 0,
        levels = this?.levels?.map { it.asEntity } ?: emptyList(),
        maxCardId = MAX_CARD_ID
    )

private val GameRules.Level.asEntity
    get() = Level(
        numberOfBoxes = numberOfBoxes ?: 0,
        pointsPerMatch = pointsPerMatch ?: 0
    )