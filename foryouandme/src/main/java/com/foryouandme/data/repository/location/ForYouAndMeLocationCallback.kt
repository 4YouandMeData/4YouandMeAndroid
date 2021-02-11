package com.foryouandme.data.repository.location

import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.repository.location.database.toHomeDatabaseEntity
import com.foryouandme.entity.location.LocationCoordinates
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForYouAndMeLocationCallback @Inject constructor(
    private val database: ForYouAndMeDatabase
) : LocationCallback() {

    private val locationFlow: MutableSharedFlow<LocationCoordinates> = MutableSharedFlow()
    val location: SharedFlow<LocationCoordinates> = locationFlow

    override fun onLocationResult(locationResult: LocationResult?) {

        if (locationResult?.lastLocation != null) {

            GlobalScope.launchSafe {

                val location = locationResult.lastLocation.toLocationCoordinates()
                database.homeLocationDao().insertLocation(location.toHomeDatabaseEntity())
                locationFlow.emit(location)

            }
        }
    }

}
