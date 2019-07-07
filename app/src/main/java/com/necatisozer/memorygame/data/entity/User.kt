package com.necatisozer.memorygame.data.entity

data class User(
    val email: String,
    val username: String,
    val photoUrl: String?,
    val highestScore: Int
)