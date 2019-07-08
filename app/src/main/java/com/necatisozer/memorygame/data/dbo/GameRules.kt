package com.necatisozer.memorygame.data.dbo

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class GameRules(
    val duration: Int? = null,
    val extraPointsPerSecond: Int? = null,
    val levels: List<Level>? = null
) {
    @IgnoreExtraProperties
    data class Level(
        val numberOfBoxes: Int? = null,
        val pointsPerMatch: Int? = null
    )
}

