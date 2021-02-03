package com.foryouandme.data.repository.device.network.request

import com.foryouandme.data.datasource.network.SerializeNulls
import com.squareup.moshi.Json

data class DeviceInfoRequest(
    @Json(name = "phone_event") val deviceInfoDataRequest: DeviceInfoDataRequest
)

data class DeviceInfoDataRequest(
    @SerializeNulls @Json(name = "battery_level") val batteryLevel: Float?,
    @SerializeNulls @Json(name = "longitude") val longitude: Double?,
    @SerializeNulls @Json(name = "latitude") val latitude: Double?,
    @SerializeNulls @Json(name = "time_zone") val timeZone: String?,
    @SerializeNulls @Json(name = "hashed_ssid") val hashedSSID: String?,
    @SerializeNulls @Json(name = "timestamp") val timestamp: Long
)