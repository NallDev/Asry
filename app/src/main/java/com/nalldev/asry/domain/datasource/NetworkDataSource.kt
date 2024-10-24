package com.nalldev.asry.domain.datasource

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import io.reactivex.rxjava3.core.Single

interface NetworkDataSource {
    fun register(request: RegisterRequestEntity): Single<RegisterResponseEntity>
    fun login(request: LoginRequestEntity) : Single<LoginResponseEntity>
    fun fetchStories(page: Int?, size: Int?, storyType: Int): Single<StoriesResponseEntity>
}