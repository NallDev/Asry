package com.nalldev.asry.domain.usecases.auth

import androidx.core.util.PatternsCompat
import io.reactivex.rxjava3.core.Observable

class LoginFormValidatorUseCase {
    operator fun invoke(
        emailObservable: Observable<String>,
        passwordObservable: Observable<String>
    ): Observable<Boolean> {
        return Observable.combineLatest(
            emailObservable.map { PatternsCompat.EMAIL_ADDRESS.matcher(it).matches() },
            passwordObservable.map { it.isNotEmpty() }
        ) { isEmailValid, isPasswordValid ->
            isEmailValid && isPasswordValid
        }
            .distinctUntilChanged()
    }
}