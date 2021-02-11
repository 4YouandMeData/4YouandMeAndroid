package com.foryouandme.domain.usecase.device

import com.foryouandme.entity.device.DeviceInfo
import com.foryouandme.entity.location.LocationCoordinates
import java.util.*

interface DeviceRepository {

    suspend fun trackDeviceInfo(
        homeLocation: LocationCoordinates?,
        currentLocation: LocationCoordinates?
    )

    suspend fun saveDeviceInfo(deviceInfo: DeviceInfo)

    suspend fun getDeviceInfo(): List<DeviceInfo>

    suspend fun deleteDeviceInfoOlderThan(timestamp: Date)

    suspend fun deleteDeviceInfo(timestamp: Date)

    suspend fun sendDeviceInfo(token: String, deviceInfo: DeviceInfo, locationPermission: Boolean)

}