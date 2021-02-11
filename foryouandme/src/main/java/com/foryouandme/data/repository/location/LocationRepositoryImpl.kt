package com.foryouandme.data.repository.location

import android.annotation.SuppressLint
import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.core.ext.isTerminated
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.repository.location.database.toHomeDatabaseEntity
import com.foryouandme.domain.usecase.location.LocationRepository
import com.foryouandme.entity.location.LocationCoordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val database: ForYouAndMeDatabase,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest,
    private val locationCallback: ForYouAndMeLocationCallback,
    private val locationLooper: LocationLooper
) : LocationRepository {

    private var currentLocationJob: Job? = null


    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(timeoutMillis: Long): LocationCoordinates? =

        if (currentLocationJob.isTerminated.not())
            withTimeoutOrNull(timeoutMillis) {
                locationCallback.location.take(1).singleOrNull()
            }
        else
            suspendCoroutine {
                currentLocationJob =
                    GlobalScope.launchSafe {

                        GlobalScope.launchSafe {

                            val location =
                                withTimeoutOrNull(timeoutMillis) {
                                    locationCallback.location.take(1).singleOrNull()
                                }

                            stopLocationTracking()
                            it.resume(location)

                        }

                        stopLocationTracking()

                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest, locationCallback, locationLooper.looper
                        )

                    }
            }

    private suspend fun stopLocationTracking() {

        catchToNullSuspend {

            fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        }

    }

    override suspend fun getHomeLocation(): LocationCoordinates? =
        database.homeLocationDao().getLocations().firstOrNull()?.toLocationCoordinates()

}