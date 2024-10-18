package com.nalldev.asry.presentation.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalldev.asry.domain.usecases.splash.CheckUserSessionUseCase
import com.nalldev.asry.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkUserSessionUseCase: CheckUserSessionUseCase
) : ViewModel() {

    private val _motionProgress : MutableLiveData<Float> = MutableLiveData(0.0f)
    val motionProgress : LiveData<Float> = _motionProgress

    private val _navigationEvent = SingleLiveEvent<NavigationDestination>()
    val navigationEvent: LiveData<NavigationDestination> = _navigationEvent

    private val disposables = CompositeDisposable()

    fun checkUserSession() {
        val disposable = checkUserSessionUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ hasSession ->
                if (hasSession) {
                    _navigationEvent.postValue(NavigationDestination.MAIN_ACTIVITY)
                } else {
                    _navigationEvent.postValue(NavigationDestination.AUTH_ACTIVITY)
                }
            }, { _ ->
                _navigationEvent.postValue(NavigationDestination.AUTH_ACTIVITY)
            })

        disposables.add(disposable)
    }

    fun storeMotionProgress(progress: Float) {
        _motionProgress.postValue(progress)
    }

    enum class NavigationDestination {
        MAIN_ACTIVITY,
        AUTH_ACTIVITY
    }
}