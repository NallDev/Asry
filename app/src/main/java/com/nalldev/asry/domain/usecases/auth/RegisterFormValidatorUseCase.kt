package com.nalldev.asry.domain.usecases.auth

import androidx.core.util.PatternsCompat
import io.reactivex.rxjava3.core.Observable

class RegisterFormValidatorUseCase {
    operator fun invoke(
        nameObservable: Observable<String>,
        emailObservable: Observable<String>,
        passwordObservable: Observable<String>
    ): Observable<Boolean> {
        return Observable.combineLatest(
            nameObservable.map { it.isNotEmpty() },
            emailObservable.map { PatternsCompat.EMAIL_ADDRESS.matcher(it).matches() },
            passwordObservable.map { it.length >= 8 }
        ) { isNameValid, isEmailValid, isPasswordValid ->
            isNameValid && isEmailValid && isPasswordValid
        }
            .distinctUntilChanged()
    }
}