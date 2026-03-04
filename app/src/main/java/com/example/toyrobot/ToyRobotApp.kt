package com.example.toyrobot

import android.app.Application
import com.example.toyrobot.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ToyRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ToyRobotApp)
            modules(appModule)
        }
    }
}

