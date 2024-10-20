package com.nalldev.asry.di

import com.nalldev.asry.data.repositories.UserSessionRepositoryImpl
import com.nalldev.asry.domain.datasource.PreferencesDataSource
import com.nalldev.asry.domain.repositories.UserSessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonRepositoryModule {
    @Provides
    @Singleton
    fun provideUserSessionRepository(preferencesDataSource: PreferencesDataSource) : UserSessionRepository =
        UserSessionRepositoryImpl(preferencesDataSource)
}