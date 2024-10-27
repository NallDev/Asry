package com.nalldev.asry.domain.datasource

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.BaseResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface NetworkDataSource {
    fun register(request: RegisterRequestEntity): Single<BaseResponseEntity>
    fun login(request: LoginRequestEntity) : Single<LoginResponseEntity>
    fun fetchStories(page: Int?, size: Int?, storyType: Int): Single<StoriesResponseEntity>
    fun addStory(
        data: Map<String, RequestBody>,
        photo: MultipartBody.Part
    ): Single<BaseResponseEntity>
}