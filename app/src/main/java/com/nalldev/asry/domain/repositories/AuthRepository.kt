package com.nalldev.asry.domain.repositories

import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import io.reactivex.rxjava3.core.Single

interface AuthRepository {
    fun register(request: RegisterRequestModel): Single<RegisterResponseModel>
    fun login(request: LoginRequestModel): Single<LoginResponseModel>
}