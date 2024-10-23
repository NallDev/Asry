package com.nalldev.asry.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nalldev.asry.data.datasource.local.LocalDataSourceImpl
import com.nalldev.asry.data.datasource.local.dao.RemoteKeysDao
import com.nalldev.asry.data.datasource.local.dao.StoryDao
import com.nalldev.asry.data.datasource.network.NetworkDataSourceImpl
import com.nalldev.asry.data.datasource.network.api.ApiService
import com.nalldev.asry.data.datasource.preference.PreferencesDataSourceImpl
import com.nalldev.asry.domain.datasource.LocalDataSource
import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.domain.datasource.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(apiService: ApiService): NetworkDataSource = NetworkDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideLocalDataSource(storyDao: StoryDao, remoteKeysDao: RemoteKeysDao): LocalDataSource = LocalDataSourceImpl(storyDao, remoteKeysDao)

    @Provides
    @Singleton
    fun providePreferencesDataSource(sharedPreferences: SharedPreferences, gson: Gson): PreferencesDataSource = PreferencesDataSourceImpl(sharedPreferences, gson)
}