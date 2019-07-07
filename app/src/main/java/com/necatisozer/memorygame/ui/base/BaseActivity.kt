package com.necatisozer.memorygame.ui.base

import androidx.appcompat.app.AppCompatActivity
import splitties.toast.toast

abstract class BaseActivity : AppCompatActivity() {
    protected fun showFailure(message: String) {
        toast(message)
    }
}