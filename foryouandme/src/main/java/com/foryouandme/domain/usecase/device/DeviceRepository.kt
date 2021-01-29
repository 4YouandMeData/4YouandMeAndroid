package com.foryouandme.domain.usecase.device

import com.foryouandme.entity.device.DeviceLocation

interface DeviceRepository {

    suspend fun getCurrentBatteryLevel(): Float?

    suspend fun getLastKnownLocation(): DeviceLocation?

    suspend fun getTimeZone(): String

    suspend fun getHashedSSID(): String

}