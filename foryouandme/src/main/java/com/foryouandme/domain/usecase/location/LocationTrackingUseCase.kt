package com.foryouandme.domain.usecase.location

import javax.inject.Inject

class LocationTrackingUseCase @Inject constructor(
    private val repository: LocationRepository,
) {

    suspend fun stop() {

        repository.stopLocationTracking()

    }

    suspend fun start() {

        repository.startLocationTracking()

    }

}