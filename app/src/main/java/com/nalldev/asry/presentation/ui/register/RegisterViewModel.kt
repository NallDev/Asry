package com.nalldev.asry.presentation.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalldev.asry.R
import com.nalldev.asry.domain.models.RegisterRequestModel
import com.nalldev.asry.domain.models.RegisterResponseModel
import com.nalldev.asry.domain.usecases.RegisterUseCase
import com.nalldev.asry.util.RemoteHelper
import com.nalldev.asry.util.SingleLiveEvent
import com.nalldev.asry.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {
    private val _registerResult = MutableLiveData<UIState<RegisterResponseModel>>()
    val registerResult: LiveData<UIState<RegisterResponseModel>> get() = _registerResult

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val disposables = CompositeDisposable()

    fun register(request: RegisterRequestModel) {
        _registerResult.postValue(UIState.Loading)
        val disposable =  registerUseCase(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                _registerResult.postValue(UIState.Success(data))
            }, { throwable ->
                val message = mapError(throwable)
                _toastEvent.postValue(message)
                _registerResult.postValue(UIState.Error)
            })

        disposables.add(disposable)
    }

    private fun mapError(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> context.getString(R.string.no_internet_connection)
            is HttpException -> RemoteHelper.remoteErrorMessage(context, throwable.code())
            else -> context.getString(R.string.error_code_default)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}