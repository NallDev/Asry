package com.nalldev.asry.di

import com.nalldev.asry.data.datasource.network.NetworkDataSource
import com.nalldev.asry.data.datasource.preference.PreferencesDataSource
import com.nalldev.asry.data.repositories.RegisterRepositoryImpl
import com.nalldev.asry.data.repositories.SplashRepositoryImpl
import com.nalldev.asry.domain.repositories.RegisterRepository
import com.nalldev.asry.domain.repositories.SplashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideRegisterRepository(networkDataSource: NetworkDataSource) : RegisterRepository =
        RegisterRepositoryImpl(networkDataSource)

    @Provides
    @ViewModelScoped
    fun provideSplashRepository(preferencesDataSource: PreferencesDataSource) : SplashRepository =
        SplashRepositoryImpl(preferencesDataSource)

}