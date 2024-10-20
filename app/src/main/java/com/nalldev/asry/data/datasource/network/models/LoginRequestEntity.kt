package com.nalldev.asry.data.datasource.network.models

import com.google.gson.annotations.SerializedName

data class LoginRequestEntity(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String
)
