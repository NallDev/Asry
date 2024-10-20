package com.nalldev.asry.data.datasource.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.domain.datasource.LocalDataSource
import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.util.Mapper
import com.nalldev.asry.util.StoryType
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) : RxRemoteMediator<Int, StoryEntity>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): Single<MediatorResult> {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> {
                return Single.just(
                    MediatorResult.Success(endOfPaginationReached = true)
                )
            }
            LoadType.APPEND -> {
                (state.pages.size) + 1
            }
        }

        return networkDataSource.fetchStories(page, state.config.pageSize, StoryType.ALL_STORY.id)
            .subscribeOn(Schedulers.io())
            .map<MediatorResult> { storiesResponseEntity ->
                val stories = Mapper.storiesNetworkToEntity(storiesResponseEntity)

                if (loadType == LoadType.REFRESH) {
                    localDataSource.removeStories().blockingAwait()
                }
                localDataSource.putStories(stories).blockingAwait()

                MediatorResult.Success(endOfPaginationReached = stories.isEmpty())
            }
            .onErrorReturn { MediatorResult.Error(it) }
    }
}