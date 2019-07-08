package com.necatisozer.memorygame.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.necatisozer.memorygame.data.dbo.DbUser
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.extension.EMPTY
import com.necatisozer.memorygame.extension.isNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    companion object {
        const val USERS_PATH = "users"
        const val HIGHEST_SCORE_FIELD = "highestScore"
        const val SCORES_FIELD = "scores"
        const val LEADERBOARD_USER_COUNT = 10
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        auth.currentUser.isNotNull()
    }

    suspend fun getUser(): User = withContext(Dispatchers.Default) {
        val currentUser = auth.currentUser ?: error("User should be logged in")
        val userSnapshot = db.collection(USERS_PATH).document(currentUser.uid).get().await()
        val dbUser = userSnapshot.toObject<DbUser>() ?: createDbUser()

        dbUser.asEntity
    }

    private suspend fun createDbUser(): DbUser = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser ?: error("User should be logged in")
        val email = currentUser.email ?: error("User should provide an email")

        val displayName = currentUser.displayName
        val username = if (displayName.isNullOrBlank()) email.substringBefore('@') else displayName

        val photoUrl = currentUser.photoUrl

        val dbUser = DbUser(
            email = email,
            username = username,
            photoUrl = photoUrl?.toString()
        )

        db.collection(USERS_PATH).document(currentUser.uid).set(dbUser).await()

        dbUser
    }

    suspend fun getLeaderboard(): List<User> = withContext(Dispatchers.IO) {
        val leaderboardSnapshot = db.collection(USERS_PATH)
            .orderBy(HIGHEST_SCORE_FIELD, Query.Direction.DESCENDING)
            .limit(LEADERBOARD_USER_COUNT.toLong())
            .get()
            .await()

        val leaderboard = leaderboardSnapshot.toObjects<DbUser>()

        leaderboard.map { it.asEntity }
    }

    suspend fun addNewScore(score: Int): Unit = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser ?: error("User should be logged in")
        val userReference = db.collection(USERS_PATH).document(currentUser.uid)
        val highestScore = getUser().highestScore

        val dbScore = DbUser.Score(
            score = score,
            createdAt = Date()
        )

        userReference.update(SCORES_FIELD, FieldValue.arrayUnion(dbScore)).await()

        if (score > highestScore) {
            userReference.update(HIGHEST_SCORE_FIELD, score).await()
        }
    }

    private val DbUser.asEntity
        get() = User(
            email = email ?: String.EMPTY,
            highestScore = highestScore ?: 0,
            photoUrl = photoUrl,
            username = username ?: String.EMPTY
        )
}