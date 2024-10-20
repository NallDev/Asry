package com.nalldev.asry.data.datasource.local

import androidx.paging.PagingSource
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.domain.datasource.LocalDataSource
import io.reactivex.rxjava3.core.Completable

class LocalDataSourceImpl(
    private val storyDao: StoryDao,
) : LocalDataSource {
    override fun putStories(stories: List<StoryEntity>): Completable {
        return Completable.fromAction {
            storyDao.putStories(stories)
        }
    }

    override fun getStories(): PagingSource<Int, StoryEntity> {
        return storyDao.getStories()
    }

    override fun removeStories(): Completable {
        return storyDao.removeStories()
    }
}