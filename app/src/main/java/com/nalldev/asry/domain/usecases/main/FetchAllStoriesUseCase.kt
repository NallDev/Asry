package com.nalldev.asry.domain.usecases.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.PagingData
import androidx.paging.map
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.repositories.MainRepository
import com.nalldev.asry.util.Mapper
import javax.inject.Inject

class FetchAllStoriesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke() : LiveData<PagingData<StoryModel>> {
        return mainRepository.fetchStories().map { listStoryEntity ->
            listStoryEntity.map { storyEntity ->
                Mapper.storyToDomain(storyEntity)
            }
        }
    }
}