package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.LocationCoordinates

interface LocationRepository {

    suspend fun getCurrentLocation(timeoutMillis: Long): LocationCoordinates?

    suspend fun getHomeLocation(): LocationCoordinates?

}