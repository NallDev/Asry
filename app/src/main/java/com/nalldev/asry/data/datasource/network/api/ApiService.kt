package com.nalldev.asry.data.datasource.network.api

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.BaseResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequestEntity
    ): Single<BaseResponseEntity>

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

    @Multipart
    @POST("stories")
    fun addStory(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part
    ): Single<BaseResponseEntity>
}