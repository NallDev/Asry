package com.nalldev.asry.domain.usecases.auth

import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.BaseResponseModel
import com.nalldev.asry.domain.repositories.AuthRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: RegisterRequestModel): Single<BaseResponseModel> {
        return authRepository.register(request)
    }
}