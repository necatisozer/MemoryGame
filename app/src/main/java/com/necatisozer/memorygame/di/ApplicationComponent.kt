package com.necatisozer.memorygame.di

import android.content.Context
import com.necatisozer.memorygame.ui.game.GameViewModel
import com.necatisozer.memorygame.ui.game.level.LevelViewModel
import com.necatisozer.memorygame.ui.main.leaderboard.LeaderboardViewModel
import com.necatisozer.memorygame.ui.main.main.MainViewModel
import com.necatisozer.memorygame.ui.splash.SplashViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    // View Models
    val splashViewModel: SplashViewModel
    val mainViewModel: MainViewModel
    val leaderboardViewModel: LeaderboardViewModel
    val gameViewModel: GameViewModel
    val levelViewModel: LevelViewModel
}