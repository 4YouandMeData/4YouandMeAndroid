package com.foryouandme.domain.usecase.device

import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.data.ext.getTimestampDateUTC
import com.foryouandme.domain.usecase.permission.IsPermissionGrantedUseCase
import com.foryouandme.entity.device.DeviceInfo
import com.foryouandme.entity.permission.Permission
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    private val repository: DeviceRepository,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase
) {

    suspend operator fun invoke(): DeviceInfo =
        coroutineScope {

            val batteryLevel = async { catchToNullSuspend { repository.getCurrentBatteryLevel() } }
            val location =
                async {
                    catchToNullSuspend {
                        if (isPermissionGrantedUseCase.invoke(Permission.Location))
                            repository.getLastKnownLocation()
                        else
                            null
                    }
                }
            val timeZone = async { catchToNullSuspend { repository.getTimeZone() } }
            val hashedSSID = async { catchToNullSuspend { repository.getHashedSSID() } }

            DeviceInfo(
                batteryLevel.await(),
                location.await(),
                timeZone.await(),
                hashedSSID.await(),
                getTimestampDateUTC()
            )

        }

}