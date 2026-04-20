package com.example.carelyze.di

import com.example.carelyze.data.api.ApiService
import com.example.carelyze.data.repository.AdviceRepositoryImpl
import com.example.carelyze.data.repository.AuthRepositoryImpl
import com.example.carelyze.data.repository.NnRepositoryImpl
import com.example.carelyze.data.repository.ScannerRepositoryImpl
import com.example.carelyze.data.local.PreferencesManager
import com.example.carelyze.domain.repository.AdviceRepository
import com.example.carelyze.domain.repository.AuthRepository
import com.example.carelyze.domain.repository.NnRepository
import com.example.carelyze.domain.repository.ScannerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindAdviceRepository(impl: AdviceRepositoryImpl): AdviceRepository

    @Binds
    @Singleton
    abstract fun bindScannerRepository(impl: ScannerRepositoryImpl): ScannerRepository

    @Binds
    @Singleton
    abstract fun bindNnRepository(impl: NnRepositoryImpl): NnRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideAuthRepositoryImpl(
            apiService: ApiService,
            preferencesManager: PreferencesManager,
        ): AuthRepositoryImpl {
            return AuthRepositoryImpl(apiService, preferencesManager)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideScannerRepositoryImpl(): ScannerRepositoryImpl {
            return ScannerRepositoryImpl()
        }
    }
}
