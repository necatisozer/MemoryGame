package com.necatisozer.memorygame.ui.main.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.necatisozer.memorygame.data.UserRepository
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LeaderboardViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val leaderboardLiveData = MutableLiveData<List<User>>()
    fun leaderboardLiveData(): LiveData<List<User>> = leaderboardLiveData

    init {
        viewModelScope.launch {
            runCatching {
                val leaderboard = userRepository.getLeaderboard()
                leaderboardLiveData.value = leaderboard
            }.onFailure { ::handleFailure }
        }
    }
}