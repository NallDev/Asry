package com.nalldev.asry.util

import com.nalldev.asry.data.datasource.network.models.LoginRequestEntity
import com.nalldev.asry.data.datasource.network.models.LoginResponseEntity
import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
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
}