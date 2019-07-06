package com.necatisozer.memorygame.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException

abstract class BaseViewModel : ViewModel() {

    protected fun handleFailure(throwable: Throwable) {
        if (throwable is CancellationException) return

        // Handle common failures here
    }
}