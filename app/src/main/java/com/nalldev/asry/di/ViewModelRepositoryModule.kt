package com.nalldev.asry.di

import com.nalldev.asry.data.datasource.local.db.StoryDatabase
import com.nalldev.asry.data.datasource.paging.StoryRemoteMediator
import com.nalldev.asry.data.repositories.AuthRepositoryImpl
import com.nalldev.asry.data.repositories.MainRepositoryImpl
import com.nalldev.asry.data.repositories.MapsRepositoryImpl
import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.domain.repositories.AuthRepository
import com.nalldev.asry.domain.repositories.MainRepository
import com.nalldev.asry.domain.repositories.MapsRepository
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

    @Provides
    @ViewModelScoped
    fun provideMainRepository(storyDatabase: StoryDatabase, remoteMediator: StoryRemoteMediator) : MainRepository =
        MainRepositoryImpl(storyDatabase, remoteMediator)

    @Provides
    @ViewModelScoped
    fun provideMapsRepository(networkDataSource: NetworkDataSource) : MapsRepository =
        MapsRepositoryImpl(networkDataSource)
}