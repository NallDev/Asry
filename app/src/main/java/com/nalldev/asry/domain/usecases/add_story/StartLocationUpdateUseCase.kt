package com.nalldev.asry.domain.usecases.add_story

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import javax.inject.Inject

class StartLocationUpdateUseCase @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val context: Context
) {
    operator fun invoke (locationCallback: LocationCallback) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                ContextCompat.getMainExecutor(context),
                locationCallback
            )
        }
    }
}