package com.nalldev.asry.domain.models

data class RegisterRequestModel(
    val name: String,
    val email: String,
    val password: String
)
