package com.nalldev.asry.util

import com.nalldev.asry.data.datasource.network.models.RegisterRequestEntity
import com.nalldev.asry.data.datasource.network.models.RegisterResponseEntity
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel

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
}