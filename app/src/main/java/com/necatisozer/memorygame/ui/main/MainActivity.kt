package com.necatisozer.memorygame.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.databinding.ActivityMainBinding
import com.necatisozer.memorygame.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
}