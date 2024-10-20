package com.nalldev.asry.util

import com.nalldev.asry.data.datasource.local.models.StoryEntity
import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.data.datasource.network.models.StoriesResponseEntity
import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.models.UserModel

object Mapper {
    fun registerToEntity(domain: RegisterRequestModel): RegisterRequestEntity {
        return RegisterRequestEntity(
            name = domain.name,
            email = domain.email,
            password = domain.password
        )
    }

    fun registerToDomain(entity: RegisterResponseEntity): RegisterResponseModel {
        return RegisterResponseModel(
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
}