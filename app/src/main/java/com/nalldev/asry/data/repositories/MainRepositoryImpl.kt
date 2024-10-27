package com.nalldev.asry.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.nalldev.asry.data.datasource.local.db.StoryDatabase
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.data.datasource.paging.StoryRemoteMediator
import com.nalldev.asry.domain.repositories.MainRepository
import io.reactivex.rxjava3.core.BackpressureStrategy
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val remoteMediator: StoryRemoteMediator
) : MainRepository {

    override fun fetchStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { storyDatabase.storyDao().getStories() }
        ).observable.toFlowable(BackpressureStrategy.LATEST).toLiveData()
    }
}