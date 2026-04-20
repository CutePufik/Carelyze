package com.example.carelyze.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.carelyze.data.local.PreferencesManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        DomainModule::class,
        NetworkModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    fun preferencesManager(): PreferencesManager

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

}