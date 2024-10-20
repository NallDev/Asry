package com.nalldev.asry.data.repositories

import com.nalldev.asry.domain.datasource.NetworkDataSource
import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.repositories.AuthRepository
import com.nalldev.asry.util.Mapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : AuthRepository {

    override fun register(request: RegisterRequestModel): Single<RegisterResponseModel> {
        val requestEntity = Mapper.registerToEntity(request)
        return networkDataSource.register(requestEntity)
            .map { responseEntity ->
                Mapper.registerToDomain(responseEntity)
            }
    }

    override fun login(request: LoginRequestModel): Single<LoginResponseModel> {
        val requestEntity = Mapper.loginToEntity(request)
        return networkDataSource.login(requestEntity)
            .map { responseEntity ->
                Mapper.loginToDomain(responseEntity)
            }
    }
}