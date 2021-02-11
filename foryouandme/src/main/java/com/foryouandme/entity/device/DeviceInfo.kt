package com.foryouandme.entity.device

import com.foryouandme.entity.location.LocationCoordinates
import java.util.*

data class DeviceInfo(
    val batteryLevel: Float?, // form 0 to 1 values
    val location: LocationCoordinates?,
    val timeZone: String?,
    val hashedSSID: String?,
    val timestamp: Date
)