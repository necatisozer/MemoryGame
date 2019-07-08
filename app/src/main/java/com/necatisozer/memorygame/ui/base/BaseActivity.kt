package com.necatisozer.memorygame.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import org.threeten.bp.Instant
import splitties.toast.toast
import kotlin.random.Random

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Just for demo... will be managed as button or switch
        delegate.localNightMode = when (Random(Instant.now().toEpochMilli()).nextBoolean()) {
            true -> AppCompatDelegate.MODE_NIGHT_YES
            false -> AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    protected fun showFailure(message: String) {
        toast(message)
    }
}