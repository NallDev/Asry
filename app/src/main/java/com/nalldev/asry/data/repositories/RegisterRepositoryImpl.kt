package com.nalldev.asry.data.repositories

import com.nalldev.asry.data.datasource.network.NetworkDataSource
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.repositories.RegisterRepository
import com.nalldev.asry.util.Mapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RegisterRepository {

    override fun register(request: RegisterRequestModel): Single<RegisterResponseModel> {
        val requestEntity = Mapper.registerToEntity(request)
        return networkDataSource.register(requestEntity)
            .map { responseEntity ->
                Mapper.registerToDomain(responseEntity)
            }
    }
}