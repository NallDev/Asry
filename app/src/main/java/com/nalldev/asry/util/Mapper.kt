package com.nalldev.asry.util

import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.data.datasource.network.models.ListStoryItem
import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.BaseResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import com.nalldev.asry.domain.models.AddStoryRequestModel
import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.BaseResponseModel
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.models.UserModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object Mapper {
    fun registerToEntity(domain: RegisterRequestModel): RegisterRequestEntity {
        return RegisterRequestEntity(
            name = domain.name,
            email = domain.email,
            password = domain.password
        )
    }

    fun baseResponseToDomain(entity: BaseResponseEntity): BaseResponseModel {
        return BaseResponseModel(
            isError = entity.error,
            message = entity.message
        )
    }

    fun loginToEntity(domain: LoginRequestModel): LoginRequestEntity {
        return LoginRequestEntity(
            email = domain.email,
            password = domain.password
        )
    }

    fun loginToDomain(entity: LoginResponseEntity): LoginResponseModel {
        return LoginResponseModel(
            isError = entity.error,
            userModel = UserModel(
                userId = entity.loginResult.userId,
                token = entity.loginResult.token,
                name = entity.loginResult.name
            )
        )
    }

    fun storiesNetworkToEntity(entity: StoriesResponseEntity): List<StoryEntity> {
        return entity.listStory.map {
            StoryEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                photoUrl = it.photoUrl,
                createdAt = it.createdAt,
                lat = it.lat,
                lon = it.lon
            )
        }
    }

    fun storyToDomain(entity: StoryEntity): StoryModel {
        return StoryModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            photoUrl = entity.photoUrl,
            createdAt = entity.createdAt,
            lat = entity.lat,
            lon = entity.lon
        )
    }

    fun storiesToDomain(listStoryItem: List<ListStoryItem>) : List<StoryModel> {
        return listStoryItem.map { story ->
            StoryModel(
                id = story.id,
                name = story.name,
                description = story.description,
                photoUrl = story.photoUrl,
                createdAt = story.createdAt,
                lat = story.lat,
                lon = story.lon
            )
        }
    }

    fun addStoryToEntity(request: AddStoryRequestModel): Pair<Map<String, RequestBody>, MultipartBody.Part> {
        val dataMap = mutableMapOf<String, RequestBody>()

        dataMap["description"] = request.description.toRequestBody("text/plain".toMediaTypeOrNull())

        request.latitude?.let {
            dataMap["lat"] = it.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        }

        request.longitude?.let {
            dataMap["lon"] = it.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        }

        val photoRequestBody = request.photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("photo", request.photo.name, photoRequestBody)

        return Pair(dataMap, photoPart)
    }
}