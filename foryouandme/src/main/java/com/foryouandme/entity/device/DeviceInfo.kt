package com.foryouandme.entity.device

data class DeviceInfo(
    val batteryLevel: Float?, // form 0 to 1 values
    val location: DeviceLocation?,
    val timeZone: String,
    val hashedSSID: String
)