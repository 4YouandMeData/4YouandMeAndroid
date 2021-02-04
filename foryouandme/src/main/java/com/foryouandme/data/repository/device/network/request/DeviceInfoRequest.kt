package com.foryouandme.data.repository.device.network.request

import com.foryouandme.data.datasource.network.SerializeNulls
import com.foryouandme.entity.device.DeviceInfo
import com.squareup.moshi.Json

data class DeviceInfoRequest(
    @Json(name = "phone_event") val deviceInfoDataRequest: DeviceInfoEventRequest
) {

    companion object {

        fun build(deviceInfo: DeviceInfo, locationPermission: Boolean): DeviceInfoRequest =
            DeviceInfoRequest(
                DeviceInfoEventRequest(
                    DeviceInfoDataRequest(

                        batteryLevel = deviceInfo.batteryLevel,
                        longitude = deviceInfo.location?.longitude,
                        latitude = deviceInfo.location?.latitude,
                        locationPermission = if (locationPermission) "granted" else "denied",
                        timeZone = deviceInfo.timeZone,
                        hashedSSID = deviceInfo.hashedSSID,
                        timestamp = deviceInfo.timestamp.time
                    )
                )
            )

    }

}

data class DeviceInfoEventRequest(@Json(name = "data") val data: DeviceInfoDataRequest)

data class DeviceInfoDataRequest(
    @SerializeNulls @Json(name = "battery_level") val batteryLevel: Float?,
    @SerializeNulls @Json(name = "longitude") val longitude: Double?,
    @SerializeNulls @Json(name = "latitude") val latitude: Double?,
    @SerializeNulls @Json(name = "location_permission") val locationPermission: String?,
    @SerializeNulls @Json(name = "time_zone") val timeZone: String?,
    @SerializeNulls @Json(name = "hashed_ssid") val hashedSSID: String?,
    @SerializeNulls @Json(name = "timestamp") val timestamp: Long
)