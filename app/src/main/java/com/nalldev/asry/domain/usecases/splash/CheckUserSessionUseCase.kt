package com.nalldev.asry.domain.usecases.splash

import com.nalldev.asry.domain.repositories.SplashRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CheckUserSessionUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    operator fun invoke(): Single<Boolean> {
        return splashRepository.hasSession()
    }
}