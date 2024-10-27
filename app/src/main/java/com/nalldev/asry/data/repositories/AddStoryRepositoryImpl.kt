package com.nalldev.asry.data.repositories

import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.domain.models.AddStoryRequestModel
import com.nalldev.asry.domain.models.BaseResponseModel
import com.nalldev.asry.domain.repositories.AddStoryRepository
import com.nalldev.asry.util.Mapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AddStoryRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : AddStoryRepository {
    override fun addStory(request: AddStoryRequestModel): Single<BaseResponseModel> {
        val (dataMap, photoPart) = Mapper.addStoryToEntity(request)
        return networkDataSource.addStory(dataMap, photoPart)
            .map { responseEntity ->
                Mapper.baseResponseToDomain(responseEntity)
            }
    }
}