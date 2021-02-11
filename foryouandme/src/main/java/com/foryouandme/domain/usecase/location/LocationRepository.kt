package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.LocationCoordinates

interface LocationRepository {

    suspend fun startLocationTracking()

    suspend fun stopLocationTracking()

    suspend fun getHomeLocation(): LocationCoordinates?

    suspend fun saveHomeLocation(location: LocationCoordinates)

    suspend fun getCurrentLocation(): LocationCoordinates?

    suspend fun saveCurrentLocation(location: LocationCoordinates)

}