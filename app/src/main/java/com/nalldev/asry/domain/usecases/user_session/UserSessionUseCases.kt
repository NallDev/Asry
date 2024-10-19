package com.nalldev.asry.domain.usecases.user_session

data class UserSessionUseCases(
    val putUserSession: PutUserSessionUseCase,
    val getUserSession: GetUserSessionUseCase,
    val removeUserSession: RemoveUserSessionUseCase
)
