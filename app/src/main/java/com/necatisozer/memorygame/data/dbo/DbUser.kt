package com.necatisozer.memorygame.data.dbo

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class DbUser(
    val email: String? = null,
    val highestScore: Int? = null,
    val photoUrl: String? = null,
    val username: String? = null
)