package com.necatisozer.memorygame

import android.app.Application
import com.necatisozer.memorygame.di.ApplicationComponent
import com.necatisozer.memorygame.di.DaggerApplicationComponent
import com.necatisozer.memorygame.di.DaggerComponentProvider
import com.necatisozer.memorygame.extension.unsyncLazy

class MyApplication : Application(), DaggerComponentProvider {
    override val component: ApplicationComponent by unsyncLazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}