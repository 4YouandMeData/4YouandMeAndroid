package com.foryouandme.data.repository.location

import android.annotation.SuppressLint
import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.repository.location.database.toCurrentDatabaseEntity
import com.foryouandme.data.repository.location.database.toHomeDatabaseEntity
import com.foryouandme.domain.usecase.location.LocationRepository
import com.foryouandme.entity.location.LocationCoordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val database: ForYouAndMeDatabase,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val locationCallback: ForYouAndMeLocationCallback,
    private val locationLooper: LocationLooper
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun startLocationTracking() {

        stopLocationTracking()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, locationLooper.looper
        )

    }

    override suspend fun stopLocationTracking() {

        catchToNullSuspend {

            fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        }

    }

    override suspend fun getHomeLocation(): LocationCoordinates? =
        database.homeLocationDao().getLocations().firstOrNull()?.toLocationCoordinates()

    override suspend fun saveHomeLocation(location: LocationCoordinates) {
        database.homeLocationDao().insertLocation(location.toHomeDatabaseEntity())
    }

    override suspend fun getCurrentLocation(): LocationCoordinates? =
        database.currentLocationDao().getLocations().firstOrNull()?.toLocationCoordinates()

    override suspend fun saveCurrentLocation(location: LocationCoordinates) {
        database.currentLocationDao().insertLocation(location.toCurrentDatabaseEntity())
    }

}