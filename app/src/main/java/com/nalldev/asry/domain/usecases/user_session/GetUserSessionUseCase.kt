package com.nalldev.asry.domain.usecases.user_session

import com.nalldev.asry.domain.models.UserModel
import com.nalldev.asry.domain.repositories.UserSessionRepository
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class GetUserSessionUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    operator fun invoke() : Maybe<UserModel> {
        return userSessionRepository.getUserSession()
    }
}