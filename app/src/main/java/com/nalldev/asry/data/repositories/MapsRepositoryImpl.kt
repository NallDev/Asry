package com.nalldev.asry.data.repositories

import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.domain.repositories.MapsRepository
import com.nalldev.asry.util.StoryType
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : MapsRepository {
    override fun fetchStories(): Single<StoriesResponseEntity> {
        return networkDataSource.fetchStories(null, null, StoryType.STORY_WITH_LOCATION.id)
    }
}