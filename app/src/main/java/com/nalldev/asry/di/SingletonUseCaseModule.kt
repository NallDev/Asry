package com.nalldev.asry.di

import com.nalldev.asry.domain.repositories.UserSessionRepository
import com.nalldev.asry.domain.usecases.user_session.GetUserSessionUseCase
import com.nalldev.asry.domain.usecases.user_session.PutUserSessionUseCase
import com.nalldev.asry.domain.usecases.user_session.RemoveUserSessionUseCase
import com.nalldev.asry.domain.usecases.user_session.UserSessionUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonUseCaseModule {

    @Provides
    @Singleton
    fun providePutUserSessionUseCase(
        userSessionRepository: UserSessionRepository
    ) : PutUserSessionUseCase = PutUserSessionUseCase(userSessionRepository)

    @Provides
    @Singleton
    fun provideGetRegisterUseCase(
        userSessionRepository: UserSessionRepository
    ) : GetUserSessionUseCase = GetUserSessionUseCase(userSessionRepository)

    @Provides
    @Singleton
    fun provideRemoveUserSessionUseCase(
        userSessionRepository: UserSessionRepository
    ) : RemoveUserSessionUseCase = RemoveUserSessionUseCase(userSessionRepository)

    @Provides
    @Singleton
    fun provideUserSessionUseCases(
        putUserSessionUseCase: PutUserSessionUseCase,
        getUserSessionUseCase: GetUserSessionUseCase,
        removeUserSessionUseCase: RemoveUserSessionUseCase
    ): UserSessionUseCases = UserSessionUseCases(
        putUserSessionUseCase,
        getUserSessionUseCase,
        removeUserSessionUseCase
    )
}