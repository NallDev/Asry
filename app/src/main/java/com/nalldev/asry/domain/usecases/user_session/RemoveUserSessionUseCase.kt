package com.nalldev.asry.domain.usecases.user_session

import com.nalldev.asry.domain.repositories.UserSessionRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RemoveUserSessionUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    operator fun invoke(): Completable {
        return userSessionRepository.removeUserSession()
    }
}