package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.Location

interface LocationRepository {

    suspend fun getHomeLocation(): Location?

    suspend fun saveHomeLocation(location: Location)

}