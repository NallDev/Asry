package com.nalldev.asry.domain.repositories

import androidx.paging.PagingData
import com.nalldev.asry.domain.models.StoryModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MainRepository {
    fun fetchStories(page: Int, size: Int, location: Int): Flowable<PagingData<StoryModel>>
}