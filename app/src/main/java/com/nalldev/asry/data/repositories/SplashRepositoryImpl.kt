package com.nalldev.asry.data.repositories

import com.nalldev.asry.data.datasource.preference.PreferencesDataSource
import com.nalldev.asry.domain.repositories.SplashRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : SplashRepository {

    override fun hasSession(): Single<Boolean> {
        return Single.fromCallable { preferencesDataSource.hasSession() }
    }
}