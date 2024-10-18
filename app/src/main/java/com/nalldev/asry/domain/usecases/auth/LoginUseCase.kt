package com.nalldev.asry.domain.usecases.auth

import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.repositories.AuthRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(request: LoginRequestModel): Single<LoginResponseModel> {
        return authRepository.login(request)
    }
}