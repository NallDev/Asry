package com.nalldev.asry.data.datasource.network

import com.nalldev.asry.data.datasource.network.api.ApiService
import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun register(request: RegisterRequestEntity): Single<RegisterResponseEntity> {
        return apiService.register(request)
    }

    fun login(request: LoginRequestEntity) : Single<LoginResponseEntity> {
        return apiService.login(request)
    }
}