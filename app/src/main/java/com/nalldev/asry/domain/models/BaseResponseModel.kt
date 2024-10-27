package com.nalldev.asry.domain.models

data class BaseResponseModel(
    val isError: Boolean,
    val message: String
)