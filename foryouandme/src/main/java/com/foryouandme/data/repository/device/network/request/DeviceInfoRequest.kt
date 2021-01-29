package com.foryouandme.data.repository.device.network.request

import com.squareup.moshi.Json

data class DeviceInfoRequest(
    @Json(name = "battery_level") val batteryLevel: Float?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "time_zone") val timeZone: String?,
    @Json(name = "hashed_ssid") val hashedSSID: String?,
    @Json(name = "timestamp") val timestamp: Long
)