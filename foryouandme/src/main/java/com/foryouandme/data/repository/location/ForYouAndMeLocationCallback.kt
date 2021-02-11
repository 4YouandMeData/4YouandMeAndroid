package com.foryouandme.data.repository.location

import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.repository.location.database.toCurrentDatabaseEntity
import com.foryouandme.data.repository.location.database.toHomeDatabaseEntity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForYouAndMeLocationCallback @Inject constructor(
    private val database: ForYouAndMeDatabase
) : LocationCallback() {

    override fun onLocationResult(locationResult: LocationResult?) {

        if (locationResult?.lastLocation != null) {

            GlobalScope.launchSafe {

                val location = locationResult.lastLocation.toLocationCoordinates()

                database.homeLocationDao().insertLocation(location.toHomeDatabaseEntity())
                database.currentLocationDao().insertLocation(location.toCurrentDatabaseEntity())

            }
        }
    }

}
