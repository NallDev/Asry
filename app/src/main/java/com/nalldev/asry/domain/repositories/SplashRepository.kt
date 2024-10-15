package com.nalldev.asry.domain.repositories

import io.reactivex.rxjava3.core.Single

interface SplashRepository {
    fun hasSession(): Single<Boolean>
}