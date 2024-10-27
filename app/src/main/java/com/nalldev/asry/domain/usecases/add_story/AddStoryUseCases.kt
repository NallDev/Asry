package com.nalldev.asry.domain.usecases.add_story

data class AddStoryUseCases(
    val startLocationUpdateUseCase: StartLocationUpdateUseCase,
    val stopLocationUpdateUseCase: StopLocationUpdateUseCase,
    val postStoryUseCase: PostStoryUseCase,
    val postStoryValidatorUseCase: PostStoryValidatorUseCase
)
