package com.nalldev.asry.domain.repositories

import com.nalldev.asry.domain.models.UserModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface UserSessionRepository {
    fun putUserSession(user: UserModel): Completable
    fun getUserSession(): Maybe<UserModel>
    fun removeUserSession(): Completable
}