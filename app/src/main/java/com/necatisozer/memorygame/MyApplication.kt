package com.necatisozer.memorygame

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.necatisozer.memorygame.di.ApplicationComponent
import com.necatisozer.memorygame.di.DaggerApplicationComponent
import com.necatisozer.memorygame.di.DaggerComponentProvider
import com.necatisozer.memorygame.extension.debug
import com.necatisozer.memorygame.extension.unsyncLazy
import timber.log.Timber

class MyApplication : Application(), DaggerComponentProvider {
    override val component: ApplicationComponent by unsyncLazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        debug {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }

        AndroidThreeTen.init(this)
    }
}