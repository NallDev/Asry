package com.nalldev.asry.data.datasource.network

import com.nalldev.asry.data.datasource.network.api.ApiService
import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import com.nalldev.asry.domain.datasource.NetworkDataSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : NetworkDataSource {
    override fun register(request: RegisterRequestEntity): Single<RegisterResponseEntity> {
        return apiService.register(request)
    }

    override fun login(request: LoginRequestEntity) : Single<LoginResponseEntity> {
        return apiService.login(request)
    }

    override fun fetchStories(page: Int?, size: Int?, storyType: Int): Single<StoriesResponseEntity> {
        return apiService.fetchStories(page, size, storyType)
    }
}