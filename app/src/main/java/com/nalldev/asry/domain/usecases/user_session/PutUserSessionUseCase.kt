package com.nalldev.asry.domain.usecases.user_session

import com.nalldev.asry.domain.models.UserModel
import com.nalldev.asry.domain.repositories.UserSessionRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class PutUserSessionUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    operator fun invoke(user : UserModel): Completable {
        return userSessionRepository.putUserSession(user)
    }
}