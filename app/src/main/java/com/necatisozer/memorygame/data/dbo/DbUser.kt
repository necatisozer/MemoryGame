package com.necatisozer.memorygame.data.dbo

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
data class DbUser(
    val email: String? = null,
    val highestScore: Int? = null,
    val photoUrl: String? = null,
    val scores: List<Score>? = null,
    val username: String? = null
) {
    @IgnoreExtraProperties
    data class Score(
        @ServerTimestamp
        val createdAt: Date? = null,
        val score: Int? = null
    )
}
