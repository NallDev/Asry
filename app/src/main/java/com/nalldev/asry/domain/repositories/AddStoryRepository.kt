package com.nalldev.asry.domain.repositories

import com.nalldev.asry.domain.models.AddStoryRequestModel
import com.nalldev.asry.domain.models.BaseResponseModel
import io.reactivex.rxjava3.core.Single

interface AddStoryRepository {
    fun addStory(request: AddStoryRequestModel): Single<BaseResponseModel>
}