package com.nalldev.asry.data.datasource.network.api

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequestEntity
    ): Single<RegisterResponseEntity>

    @POST("login")
    fun login(
        @Body request : LoginRequestEntity
    ): Single<LoginResponseEntity>
}