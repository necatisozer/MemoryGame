package com.necatisozer.memorygame.data.entity

data class Rules(
    val duration: Int,
    val extraPointsPerSecond: Int,
    val levels: List<Level>,
    val maxCardId: Int
)

data class Level(
    val numberOfBoxes: Int,
    val pointsPerMatch: Int
)