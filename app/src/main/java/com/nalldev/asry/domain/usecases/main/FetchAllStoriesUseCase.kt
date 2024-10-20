package com.nalldev.asry.domain.usecases.main

import androidx.paging.PagingData
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.repositories.MainRepository
import com.nalldev.asry.util.StoryType
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class FetchAllStoriesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(page: Int, size: Int) : Flowable<PagingData<StoryModel>> {
        return mainRepository.fetchStories(page, size, StoryType.ALL_STORY.id)
    }
}