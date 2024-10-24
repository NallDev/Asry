package com.nalldev.asry.domain.usecases.maps

import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.repositories.MapsRepository
import com.nalldev.asry.util.Mapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FetchStoryWithCoordinateUseCase @Inject constructor(
    private val mapsRepository: MapsRepository
) {
    operator fun invoke() : Single<List<StoryModel>> {
        return mapsRepository.fetchStories().map { storyEntity ->
            Mapper.storiesToDomain(storyEntity.listStory)
        }
    }
}