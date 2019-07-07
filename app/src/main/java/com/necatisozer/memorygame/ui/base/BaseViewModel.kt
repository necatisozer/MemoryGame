package com.necatisozer.memorygame.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.necatisozer.memorygame.core.SingleLiveEvent
import kotlinx.coroutines.CancellationException

abstract class BaseViewModel : ViewModel() {
    private val failureEvent = SingleLiveEvent<String>()
    fun failureEvent(): LiveData<String> = failureEvent

    protected fun handleFailure(throwable: Throwable) {
        if (throwable is CancellationException) return

        failureEvent.value = throwable.localizedMessage
    }
}