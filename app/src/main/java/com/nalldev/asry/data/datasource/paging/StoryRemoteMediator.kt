package com.nalldev.asry.data.datasource.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.nalldev.asry.data.datasource.local.db.StoryDatabase
import com.nalldev.asry.data.datasource.local.models.RemoteKeys
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.util.Mapper
import com.nalldev.asry.util.StoryType
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val storyDatabase: StoryDatabase
) : RxRemoteMediator<Int, StoryEntity>() {

    override fun initializeSingle(): Single<InitializeAction> {
        return Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH)
    }

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): Single<MediatorResult> {

        return Single.just(loadType)
            .map { type ->
                when (type) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        val refreshKey = remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
                        Pair(refreshKey, false)
                    }

                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)

                        val prevKey = remoteKeys?.prevKey
                            ?: INVALID_PAGE

                        Pair(prevKey, remoteKeys != null)
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)

                        val nextKey = remoteKeys?.nextKey
                            ?: INVALID_PAGE

                        Pair(nextKey, remoteKeys != null)
                    }
                }
            }
            .flatMap { pair ->
                if (pair.first == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = pair.second))
                } else {
                    networkDataSource.fetchStories(
                        pair.first,
                        state.config.pageSize,
                        StoryType.ALL_STORY.id
                    )
                        .map { responseData ->
                            Mapper.storiesNetworkToEntity(responseData)
                        }
                        .flatMap <MediatorResult> { listStoryEntity ->
                            val endOfPaginationReached = listStoryEntity.isEmpty()
                            Single.fromCallable {
                                storyDatabase.runInTransaction(
                                    body = {
                                        if (loadType == LoadType.REFRESH) {
                                            storyDatabase.remoteKeyDao().removeRemoteKeys()
                                            storyDatabase.storyDao().removeStories()
                                        }
                                        val prevKey = if (pair.first == 1) null else pair.first - 1
                                        val nextKey =
                                            if (endOfPaginationReached) null else pair.first + 1
                                        val remoteKeys = listStoryEntity.map { story ->
                                            RemoteKeys(
                                                id = story.id,
                                                prevKey = prevKey,
                                                nextKey = nextKey
                                            )
                                        }
                                        storyDatabase.remoteKeyDao().putRemoteKeys(remoteKeys)
                                        storyDatabase.storyDao().putStories(listStoryEntity)
                                    }
                                )
                                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                            }
                        }
                        .onErrorReturn { throwable ->
                            MediatorResult.Error(throwable)
                        }
                }
            }
            .onErrorReturn { throwable -> MediatorResult.Error(throwable) }
            .subscribeOn(Schedulers.io())
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { story ->
            storyDatabase.remoteKeyDao().getRemoteKeysId(story.id)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { story ->
            storyDatabase.remoteKeyDao().getRemoteKeysId(story.id)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.remoteKeyDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val INVALID_PAGE = -1
    }
}