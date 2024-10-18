package com.nalldev.asry.domain.models

data class LoginResponseModel(
    val isError: Boolean,
    val userModel: UserModel
)