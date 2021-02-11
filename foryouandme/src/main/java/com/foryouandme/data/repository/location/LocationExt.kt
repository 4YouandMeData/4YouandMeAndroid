package com.foryouandme.data.repository.location

import android.location.Location
import com.foryouandme.entity.location.LocationCoordinates

internal fun Location.toLocationCoordinates(): LocationCoordinates =
    LocationCoordinates(latitude, longitude)