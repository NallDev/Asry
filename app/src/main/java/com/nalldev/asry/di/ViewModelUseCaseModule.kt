package com.nalldev.asry.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.nalldev.asry.domain.repositories.AuthRepository
import com.nalldev.asry.domain.usecases.add_story.AddStoryUseCases
import com.nalldev.asry.domain.usecases.add_story.StartLocationUpdateUseCase
import com.nalldev.asry.domain.usecases.add_story.StopLocationUpdateUseCase
import com.nalldev.asry.domain.usecases.auth.AuthUseCases
import com.nalldev.asry.domain.usecases.auth.LoginFormValidatorUseCase
import com.nalldev.asry.domain.usecases.auth.LoginUseCase
import com.nalldev.asry.domain.usecases.auth.RegisterFormValidatorUseCase
import com.nalldev.asry.domain.usecases.auth.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelUseCaseModule {

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

    @Provides
    @ViewModelScoped
    fun provideStartLocationUpdateUseCase(fusedLocationClient: FusedLocationProviderClient, locationRequest: LocationRequest, @ApplicationContext context: Context): StartLocationUpdateUseCase =
        StartLocationUpdateUseCase(fusedLocationClient, locationRequest, context)

    @Provides
    @ViewModelScoped
    fun provideStopLocationUpdateUseCase(fusedLocationClient: FusedLocationProviderClient): StopLocationUpdateUseCase =
        StopLocationUpdateUseCase(fusedLocationClient)

    @Provides
    @ViewModelScoped
    fun provideAddStoryUseCases(
        startLocationUpdateUseCase: StartLocationUpdateUseCase,
        stopLocationUpdateUseCase: StopLocationUpdateUseCase
    ): AddStoryUseCases = AddStoryUseCases(
        startLocationUpdateUseCase,
        stopLocationUpdateUseCase
    )

}