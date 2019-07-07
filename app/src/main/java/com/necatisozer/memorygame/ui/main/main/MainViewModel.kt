package com.necatisozer.memorygame.ui.main.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.necatisozer.memorygame.data.UserRepository
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val userLiveData = MutableLiveData<User>()
    fun userLiveData(): LiveData<User> = userLiveData

    init {
        viewModelScope.launch {
            runCatching {
                val user = userRepository.getUser()
                userLiveData.value = user
            }.onFailure { ::handleFailure }
        }
    }
}