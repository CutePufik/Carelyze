package com.example.carelyze.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carelyze.presentation.viewmodel.AdviceViewModel
import com.example.carelyze.presentation.viewmodel.AuthViewModel
import com.example.carelyze.presentation.viewmodel.OnboardingViewModel
import com.example.carelyze.presentation.viewmodel.ScanProcessViewModel
import com.example.carelyze.presentation.viewmodel.ScannerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap


@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ClassKey(AdviceViewModel::class)
    abstract fun bindAdviceViewModel(vm: AdviceViewModel): ViewModel

    @Binds
    @IntoMap
    @ClassKey(ScannerViewModel::class)
    abstract fun bindScannerViewModel(vm: ScannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ClassKey(ScanProcessViewModel::class)
    abstract fun bindScanProcessViewModel(vm: ScanProcessViewModel): ViewModel

    @Binds
    @IntoMap
    @ClassKey(OnboardingViewModel::class)
    abstract fun bindOnboardingViewModel(vm: OnboardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ClassKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(vm: AuthViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}