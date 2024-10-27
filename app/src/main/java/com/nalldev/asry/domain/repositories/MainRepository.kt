package com.nalldev.asry.domain.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.nalldev.asry.data.datasource.local.models.StoryEntity

interface MainRepository {
    fun fetchStories(): LiveData<PagingData<StoryEntity>>
}