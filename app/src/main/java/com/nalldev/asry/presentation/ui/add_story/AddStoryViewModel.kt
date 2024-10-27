package com.nalldev.asry.presentation.ui.add_story

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.nalldev.asry.R
import com.nalldev.asry.domain.models.AddStoryRequestModel
import com.nalldev.asry.domain.usecases.add_story.AddStoryUseCases
import com.nalldev.asry.util.CommonHelper
import com.nalldev.asry.util.Optional
import com.nalldev.asry.util.SingleLiveEvent
import com.nalldev.asry.util.UIState
import com.nalldev.asry.util.reduceFileImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val addStoryUseCase: AddStoryUseCases,
    @ApplicationContext private val context : Context
) : ViewModel() {
    private var currentUri : Uri? = null

    private val descriptionSubject = BehaviorSubject.createDefault("")
    private val coordinateSubject = BehaviorSubject.createDefault(Optional<LatLng>(null))
    private val useLocationSubject = BehaviorSubject.createDefault(false)

    val coordinate : LiveData<Optional<LatLng>> = coordinateSubject.toFlowable(BackpressureStrategy.LATEST).toLiveData()
    val useLocation : LiveData<Boolean> = useLocationSubject.toFlowable(BackpressureStrategy.LATEST).toLiveData()

    private val _addStoryResult : MutableLiveData<UIState<Unit>> = MutableLiveData()
    val addStoryResult : LiveData<UIState<Unit>> = _addStoryResult

    private val _isFormValid : MutableLiveData<Boolean> = MutableLiveData()
    val isFormValid : LiveData<Boolean> = _isFormValid

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val _navigateState = SingleLiveEvent<NavigateState>()
    val navigateState: LiveData<NavigateState> = _navigateState

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    setCoordinate(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    private val disposables = CompositeDisposable()

    init {
        observePostStoryValidation()
    }

    private fun observePostStoryValidation() {
        val disposable = addStoryUseCase.postStoryValidatorUseCase(
            descriptionSubject, useLocationSubject, coordinateSubject
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isValid ->
                _isFormValid.postValue(isValid)
            }

        disposables.add(disposable)
    }

    fun startLocationUpdates() {
        addStoryUseCase.startLocationUpdateUseCase(locationCallback)
    }

    fun stopLocationUpdates() {
        addStoryUseCase.stopLocationUpdateUseCase(locationCallback)
    }

    fun setDescription(description : String) {
        descriptionSubject.onNext(description)
    }

    fun setUseLocation(isUseLocation : Boolean) {
        useLocationSubject.onNext(isUseLocation)
    }

    fun setCurrentUri(uri: Uri) {
        currentUri = uri
    }

    fun setCoordinate(latLng: LatLng?) {
        coordinateSubject.onNext(Optional(latLng))
    }

    fun doPostStory() {
        _addStoryResult.postValue(UIState.Loading)
        try {
            val uri = currentUri ?: return
            val description = descriptionSubject.value.toString()
            val latitude = coordinateSubject.value?.value?.latitude
            val longitude = coordinateSubject.value?.value?.longitude
            val photo = uri.toFile().reduceFileImage()

            val request = AddStoryRequestModel(
                description = description,
                photo = photo,
                latitude = latitude,
                longitude = longitude
            )

            val disposable = addStoryUseCase.postStoryUseCase(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _ ->
                    _toastEvent.postValue(context.getString(R.string.post_story_success_msg))
                    _addStoryResult.postValue(UIState.Success(Unit))
                    _navigateState.postValue(NavigateState.MAIN)
                }, { throwable ->
                    val message = CommonHelper.getErrorMessage(throwable, context)
                    _toastEvent.postValue(message)
                    _addStoryResult.postValue(UIState.Error)
                })
            disposables.add(disposable)
        } catch (e : Exception) {
            _toastEvent.postValue(e.message.toString())
            _addStoryResult.postValue(UIState.Error)
        }
    }

    enum class NavigateState {
        MAIN
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}