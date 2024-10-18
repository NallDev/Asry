package com.nalldev.asry.di

import com.nalldev.asry.data.datasource.network.NetworkDataSource
import com.nalldev.asry.data.datasource.preference.PreferencesDataSource
import com.nalldev.asry.data.repositories.AuthRepositoryImpl
import com.nalldev.asry.data.repositories.SplashRepositoryImpl
import com.nalldev.asry.domain.repositories.AuthRepository
import com.nalldev.asry.domain.repositories.SplashRepository
import com.nalldev.asry.domain.usecases.auth.AuthUseCases
import com.nalldev.asry.domain.usecases.auth.LoginFormValidatorUseCase
import com.nalldev.asry.domain.usecases.auth.LoginUseCase
import com.nalldev.asry.domain.usecases.auth.RegisterFormValidatorUseCase
import com.nalldev.asry.domain.usecases.auth.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideRegisterRepository(networkDataSource: NetworkDataSource) : AuthRepository =
        AuthRepositoryImpl(networkDataSource)

    @Provides
    @ViewModelScoped
    fun provideSplashRepository(preferencesDataSource: PreferencesDataSource) : SplashRepository =
        SplashRepositoryImpl(preferencesDataSource)

    @Provides
    @ViewModelScoped
    fun provideRegisterUseCase(
        authRepository: AuthRepository
    ): RegisterUseCase = RegisterUseCase(authRepository)

    @Provides
    @ViewModelScoped
    fun provideLoginUseCase(
        authRepository: AuthRepository
    ): LoginUseCase = LoginUseCase(authRepository)

    @Provides
    @ViewModelScoped
    fun provideRegisterFormValidatorUseCase(): RegisterFormValidatorUseCase =
        RegisterFormValidatorUseCase()

    @Provides
    @ViewModelScoped
    fun provideLoginFormValidatorUseCase(): LoginFormValidatorUseCase =
        LoginFormValidatorUseCase()

    @Provides
    @ViewModelScoped
    fun provideAuthUseCases(
        registerUseCase: RegisterUseCase,
        loginUseCase: LoginUseCase,
        registerFormValidatorUseCase: RegisterFormValidatorUseCase,
        loginFormValidatorUseCase: LoginFormValidatorUseCase
    ): AuthUseCases = AuthUseCases(
        registerUseCase,
        loginUseCase,
        registerFormValidatorUseCase,
        loginFormValidatorUseCase
    )
}