package com.nalldev.asry.domain.usecases.add_story

import com.nalldev.asry.domain.models.AddStoryRequestModel
import com.nalldev.asry.domain.models.BaseResponseModel
import com.nalldev.asry.domain.repositories.AddStoryRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PostStoryUseCase @Inject constructor(
    private val repository: AddStoryRepository
) {
    operator fun invoke(request: AddStoryRequestModel): Single<BaseResponseModel> {
        return repository.addStory(request)
    }
}