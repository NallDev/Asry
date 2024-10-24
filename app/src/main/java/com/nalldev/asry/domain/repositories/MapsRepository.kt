package com.nalldev.asry.domain.repositories

import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import io.reactivex.rxjava3.core.Single

interface MapsRepository {
    fun fetchStories(): Single<StoriesResponseEntity>
}