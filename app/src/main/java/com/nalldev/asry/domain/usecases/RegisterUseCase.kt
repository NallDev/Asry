package com.nalldev.asry.domain.usecases

import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.repositories.RegisterRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    operator fun invoke(request: RegisterRequestModel): Single<RegisterResponseModel> {
        return registerRepository.register(request)
    }
}