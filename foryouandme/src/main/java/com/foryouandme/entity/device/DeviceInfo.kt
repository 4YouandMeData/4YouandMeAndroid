package com.foryouandme.entity.device

import com.foryouandme.entity.location.Location
import java.util.*

data class DeviceInfo(
    val batteryLevel: Float?, // form 0 to 1 values
    val location: Location?,
    val timeZone: String?,
    val hashedSSID: String?,
    val timestamp: Date
)