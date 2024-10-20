package com.nalldev.asry.data.datasource.network.api

import com.nalldev.asry.domain.datasource.PreferencesDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = preferencesDataSource.getUserSession()
            .map { it.token }
            .blockingGet()

        return if (token.isNullOrEmpty()) {
            chain.proceed(request)
        } else {
            val authenticatedRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(authenticatedRequest)
        }
    }
}