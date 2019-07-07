package com.necatisozer.memorygame.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.necatisozer.memorygame.core.SingleLiveEvent
import com.necatisozer.memorygame.data.UserRepository
import com.necatisozer.memorygame.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val openMainEvent = SingleLiveEvent<Void>()
    fun openMainEvent(): LiveData<Void> = openMainEvent

    private val signInEvent = SingleLiveEvent<Void>()
    fun signInEvent(): LiveData<Void> = signInEvent

    init {
        viewModelScope.launch {
            runCatching {
                when (userRepository.isLoggedIn()) {
                    true -> openMainEvent.call()
                    false -> signInEvent.call()
                }
            }.onFailure { ::handleFailure }

        }
    }
}