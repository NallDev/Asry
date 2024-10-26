package com.nalldev.asry.domain.usecases.add_story

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import javax.inject.Inject

class StopLocationUpdateUseCase @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) {
    operator fun invoke(locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}