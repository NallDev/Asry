package com.nalldev.asry.domain.models

data class StoryModel(
    val id : String,
    val name : String,
    val description : String,
    val photoUrl : String,
    val createdAt : String,
    val lat : Double?,
    val lon : Double?
)
