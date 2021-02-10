package com.foryouandme.data.repository.location

import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.repository.location.database.toDatabaseEntity
import com.foryouandme.domain.usecase.location.LocationRepository
import com.foryouandme.entity.location.Location
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val database: ForYouAndMeDatabase
): LocationRepository {

    override suspend fun getHomeLocation(): Location? =
        database.locationDao().getLocation().firstOrNull()?.toLocation()

    override suspend fun saveHomeLocation(location: Location) {
        database.locationDao().insertLocation(location.toDatabaseEntity())
    }

}