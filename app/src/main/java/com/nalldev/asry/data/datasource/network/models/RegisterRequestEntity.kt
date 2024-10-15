package com.nalldev.asry.data.datasource.network.models

import com.google.gson.annotations.SerializedName

data class RegisterRequestEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
