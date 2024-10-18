package com.nalldev.asry.domain.usecases.auth

data class AuthUseCases(
    val registerUseCase: RegisterUseCase,
    val loginUseCase: LoginUseCase,
    val registerFormValidatorUseCase: RegisterFormValidatorUseCase,
    val loginFormValidatorUseCase: LoginFormValidatorUseCase
)