package com.foryouandme.data.repository.device.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foryouandme.entity.device.DeviceInfo
import com.foryouandme.entity.location.LocationCoordinates
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "device_info")
data class DeviceInfoDatabaseEntity(
    @PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: Date,
    @Json(name = "battery_level") val batteryLevel: Float?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "time_zone") val timeZone: String?,
    @Json(name = "hashed_ssid") val hashedSSID: String?,
) {

    fun toDeviceInfo(): DeviceInfo =
        DeviceInfo(
            timestamp = timestamp,
            batteryLevel = batteryLevel,
            location =
            if (longitude != null && latitude != null)
                LocationCoordinates(
                    latitude,
                    longitude
                )
            else null,
            timeZone = timeZone,
            hashedSSID = hashedSSID
        )

}

fun DeviceInfo.toDatabaseEntity(): DeviceInfoDatabaseEntity =
    DeviceInfoDatabaseEntity(
        timestamp,
        batteryLevel,
        location?.longitude,
        location?.latitude,
        timeZone,
        hashedSSID
    )