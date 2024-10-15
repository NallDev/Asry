package com.nalldev.asry.domain.repositories

import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import io.reactivex.rxjava3.core.Single

interface RegisterRepository {
    fun register(request: RegisterRequestModel): Single<RegisterResponseModel>
}