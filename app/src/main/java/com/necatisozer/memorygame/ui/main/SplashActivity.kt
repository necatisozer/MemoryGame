package com.necatisozer.memorygame.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.viewModels

class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels { injector.splashViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {

    }
}
