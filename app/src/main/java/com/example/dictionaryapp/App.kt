package com.example.dictionaryapp

import android.app.Application
import com.example.dictionaryapp.di.appModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

    }
}