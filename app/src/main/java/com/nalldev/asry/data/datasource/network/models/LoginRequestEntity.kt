package com.nalldev.asry.data.datasource.network.models

import com.google.gson.annotations.SerializedName

data class LoginRequestEntity(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
