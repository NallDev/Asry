package com.nalldev.asry.data.repositories

import com.nalldev.asry.data.datasource.preference.PreferencesDataSource
import com.nalldev.asry.domain.models.UserModel
import com.nalldev.asry.domain.repositories.UserSessionRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserSessionRepository {
    override fun putUserSession(user: UserModel): Completable {
        return preferencesDataSource.putUserSession(user)
    }

    override fun getUserSession(): Maybe<UserModel> {
        return preferencesDataSource.getUserSession()
    }

    override fun removeUserSession(): Completable {
        return preferencesDataSource.removeUserSession()
    }
}