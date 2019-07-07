package com.necatisozer.memorygame.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.necatisozer.memorygame.data.dbo.DbUser
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.extension.isNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    companion object {
        const val USERS_PATH = "users"
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        auth.currentUser.isNull().not()
    }

    suspend fun getUser(): User = withContext(Dispatchers.Default) {
        val currentUser = auth.currentUser!!
        val userSnapshot = db.collection(USERS_PATH).document(currentUser.uid).get().await()
        val dbUser = userSnapshot.toObject<DbUser>() ?: createDbUser()

        User(
            email = currentUser.email!!,
            username = dbUser.username!!,
            photoUrl = currentUser.photoUrl?.toString(),
            highestScore = dbUser.highestScore ?: 0
        )
    }

    private suspend fun createDbUser(): DbUser = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser!!
        val username = currentUser.displayName ?: currentUser.email!!.substringBefore('@')
        val dbUser = DbUser(username = username)
        db.collection(USERS_PATH).document(currentUser.uid).set(dbUser).await()
        dbUser
    }
}