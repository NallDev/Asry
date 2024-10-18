package com.nalldev.asry.presentation.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalldev.asry.R
import com.nalldev.asry.domain.models.LoginRequestModel
import com.nalldev.asry.domain.models.LoginResponseModel
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.usecases.auth.AuthUseCases
import com.nalldev.asry.util.CommonHelper
import com.nalldev.asry.util.SingleLiveEvent
import com.nalldev.asry.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val registerNameSubject = BehaviorSubject.createDefault("")
    private val registerEmailSubject = BehaviorSubject.createDefault("")
    private val registerPasswordSubject = BehaviorSubject.createDefault("")

    private val loginEmailSubject = BehaviorSubject.createDefault("")
    private val loginPasswordSubject = BehaviorSubject.createDefault("")

    private val _motionProgress: MutableLiveData<Float> = MutableLiveData(0.0f)
    val motionProgress: LiveData<Float> = _motionProgress

    private val _transitionState: MutableLiveData<TransitionState> = MutableLiveData()
    val transitionState: LiveData<TransitionState> = _transitionState

    private val _isRegisterFormValid = MutableLiveData<Boolean>()
    val isRegisterFormValid: LiveData<Boolean> get() = _isRegisterFormValid

    private val _isLoginFormValid = MutableLiveData<Boolean>()
    val isLoginFormValid: LiveData<Boolean> get() = _isLoginFormValid

    private val _registerResult = MutableLiveData<UIState<RegisterResponseModel>>()
    val registerResult: LiveData<UIState<RegisterResponseModel>> get() = _registerResult

    private val _loginResult = MutableLiveData<UIState<LoginResponseModel>>()
    val loginResult: LiveData<UIState<LoginResponseModel>> get() = _loginResult

    private val _navigateState = SingleLiveEvent<NavigateState>()
    val navigateState: LiveData<NavigateState> = _navigateState

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val disposables = CompositeDisposable()

    init {
        observeRegisterFormValidation()
        observeLoginFormValidation()
    }

    private fun observeRegisterFormValidation() {
        val disposable = authUseCases.registerFormValidatorUseCase(
            registerNameSubject, registerEmailSubject, registerPasswordSubject
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isValid ->
                _isRegisterFormValid.postValue(isValid)
            }

        disposables.add(disposable)
    }

    private fun observeLoginFormValidation() {
        val disposable = authUseCases.loginFormValidatorUseCase(
            loginEmailSubject, loginPasswordSubject
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isValid ->
                _isLoginFormValid.postValue(isValid)
            }

        disposables.add(disposable)
    }

    fun doUserRegister() {
        val requestBody = RegisterRequestModel(
            name = registerNameSubject.value.toString(),
            email = registerEmailSubject.value.toString(),
            password = registerPasswordSubject.value.toString()
        )

        _registerResult.postValue(UIState.Loading)
        val disposable = authUseCases.registerUseCase(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                _registerResult.postValue(UIState.Success(data))
                _navigateState.postValue(NavigateState.LOGIN)
                _toastEvent.postValue(context.getString(R.string.register_success_msg))
            }, { throwable ->
                val message = CommonHelper.getErrorMessage(throwable, context)
                _toastEvent.postValue(message)
                _registerResult.postValue(UIState.Error)
            })

        disposables.add(disposable)
    }

    fun doUserLogin() {
        val requestBody = LoginRequestModel(
            email = loginEmailSubject.value.toString(),
            password = loginPasswordSubject.value.toString()
        )

        _loginResult.postValue(UIState.Loading)
        val disposable = authUseCases.loginUseCase(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                _loginResult.postValue(UIState.Success(data))
                _navigateState.postValue(NavigateState.MAIN)
                _toastEvent.postValue(context.getString(R.string.login_success_msg))
            }, { throwable ->
                val message = CommonHelper.getErrorMessage(throwable, context)
                _toastEvent.postValue(message)
                _loginResult.postValue(UIState.Error)
            })

        disposables.add(disposable)
    }

    fun storeMotionProgress(progress: Float) {
        _motionProgress.postValue(progress)
    }

    fun changeTransitionState(state: TransitionState) {
        _transitionState.postValue(state)
    }

    fun changeRegisterName(text: String) {
        registerNameSubject.onNext(text)
    }

    fun changeRegisterEmail(text: String) {
        registerEmailSubject.onNext(text)
    }

    fun changeRegisterPassword(text: String) {
        registerPasswordSubject.onNext(text)
    }

    fun changeLoginEmail(text: String) {
        loginEmailSubject.onNext(text)
    }

    fun changeLoginPassword(text: String) {
        loginPasswordSubject.onNext(text)
    }

    enum class TransitionState {
        START, END
    }

    enum class NavigateState {
        LOGIN, MAIN
    }
}