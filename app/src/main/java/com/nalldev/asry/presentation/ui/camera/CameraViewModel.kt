package com.nalldev.asry.presentation.ui.camera

import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val _cameraSelector = MutableLiveData(CameraSelector.DEFAULT_BACK_CAMERA)
    val cameraSelector: LiveData<CameraSelector> = _cameraSelector

    private val _rotation : MutableLiveData<Int> = MutableLiveData()
    val rotation : LiveData<Int> = _rotation

    var imageCapture: ImageCapture? = null

    init {
        imageCapture = ImageCapture.Builder().build()
    }

    fun swapCamera() {
        val position = if (_cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        _cameraSelector.postValue(position)
    }

    fun updateRotation(orientation: Int) {
        val rotation = when (orientation) {
            in 45 until 135 -> Surface.ROTATION_270
            in 135 until 225 -> Surface.ROTATION_180
            in 225 until 315 -> Surface.ROTATION_90
            else -> Surface.ROTATION_0
        }
        _rotation.postValue(rotation)
        imageCapture?.targetRotation = rotation
    }
}