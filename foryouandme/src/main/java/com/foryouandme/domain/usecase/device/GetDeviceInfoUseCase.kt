package com.foryouandme.domain.usecase.device

import com.foryouandme.domain.usecase.permission.IsPermissionGrantedUseCase
import com.foryouandme.entity.device.DeviceInfo
import com.foryouandme.entity.permission.Permission
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val repository: DeviceRepository,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase
) {

    suspend operator fun invoke(): DeviceInfo {

        val batteryLevel = repository.getCurrentBatteryLevel()
        val location =
            if (isPermissionGrantedUseCase.invoke(Permission.Location))
                repository.getLastKnownLocation()
            else
                null
        val timeZone = repository.getTimeZone()
        val hashedSSID = repository.getHashedSSID()

        return DeviceInfo(batteryLevel, location, timeZone, hashedSSID)

    }

}