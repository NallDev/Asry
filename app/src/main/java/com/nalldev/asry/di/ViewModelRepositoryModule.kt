package com.nalldev.asry.di

import com.nalldev.asry.data.datasource.network.NetworkDataSource
import com.nalldev.asry.data.repositories.AuthRepositoryImpl
import com.nalldev.asry.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelRepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideAuthRepository(networkDataSource: NetworkDataSource) : AuthRepository =
        AuthRepositoryImpl(networkDataSource)
}