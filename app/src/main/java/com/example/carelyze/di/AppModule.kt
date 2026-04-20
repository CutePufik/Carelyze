package com.example.carelyze.di

import android.app.Application
import android.content.Context
import com.example.carelyze.data.local.PreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun providePreferencesManager(context: Context): PreferencesManager =
        PreferencesManager(context)
}