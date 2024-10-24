package com.nalldev.asry.data.datasource.network.api

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequestEntity
    ): Single<RegisterResponseEntity>

    @POST("login")
    fun login(
        @Body request : LoginRequestEntity
    ): Single<LoginResponseEntity>

    @GET("stories")
    fun fetchStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") storyType: Int
    ): Single<StoriesResponseEntity>
}