package com.nalldev.asry.domain.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.domain.models.StoryModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

interface MainRepository {
    fun fetchStories(): Observable<PagingData<StoryEntity>>
}