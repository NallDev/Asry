package com.nalldev.asry.domain.models

import java.io.File

data class AddStoryRequestModel(
    val description: String,
    val photo: File,
    val latitude: Double?,
    val longitude: Double?
)