package com.nalldev.asry.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.flowable
import com.nalldev.asry.data.datasource.paging.StoryRemoteMediator
import com.nalldev.asry.domain.datasource.LocalDataSource
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.repositories.MainRepository
import com.nalldev.asry.util.Mapper
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteMediator: StoryRemoteMediator
) : MainRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun fetchStories(page: Int, size: Int, location: Int): Flowable<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { localDataSource.getStories() }
        ).flowable
            .map { pagingData ->
                pagingData.map { storyEntity ->
                    Mapper.storyToDomain(storyEntity)
                }
            }
    }
}