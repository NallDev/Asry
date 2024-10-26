package com.nalldev.asry.presentation.ui.add_story

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.nalldev.asry.domain.usecases.add_story.AddStoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val addStoryUseCase: AddStoryUseCases,
    @ApplicationContext private val context : Context
) : ViewModel() {
    private var currentUri : Uri? = null

    private val _coordinate : MutableLiveData<LatLng?> = MutableLiveData()
    val coordinate : MutableLiveData<LatLng?> = _coordinate

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    setCoordinate(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    fun startLocationUpdates() {
        addStoryUseCase.startLocationUpdateUseCase(locationCallback)
    }

    fun stopLocationUpdates() {
        addStoryUseCase.stopLocationUpdateUseCase(locationCallback)
    }

    fun setCoordinate(latLng: LatLng?) {
        _coordinate.postValue(latLng)
    }

    fun setCurrentUri(uri: Uri) {
        currentUri = uri
    }
}