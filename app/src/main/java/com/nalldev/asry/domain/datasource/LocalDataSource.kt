package com.nalldev.asry.domain.datasource

import androidx.paging.PagingSource
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import io.reactivex.rxjava3.core.Completable

interface LocalDataSource {
    fun putStories(stories: List<StoryEntity>) : Completable
    fun getStories(): PagingSource<Int, StoryEntity>
    fun removeStories() : Completable
}