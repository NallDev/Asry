package com.nalldev.asry.data.datasource.network.models

import com.google.gson.annotations.SerializedName

data class BaseResponseEntity(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)