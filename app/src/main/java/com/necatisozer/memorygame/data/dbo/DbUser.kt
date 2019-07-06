package com.necatisozer.memorygame.data.dbo

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class DbUser(
    val username: String? = null,
    val highestScore: Int? = null
)