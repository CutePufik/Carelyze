package com.example.carelyze.di

import android.app.Application
import kotlin.getValue

class App : Application() {

    override fun onCreate() {
        super.onCreate()

    }



    val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

}