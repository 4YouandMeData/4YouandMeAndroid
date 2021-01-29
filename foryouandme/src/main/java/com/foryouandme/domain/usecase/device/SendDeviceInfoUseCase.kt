package com.foryouandme.domain.usecase.device

import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.data.ext.getTimestampDateUTC
import com.foryouandme.data.ext.minusDays
import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.domain.usecase.permission.IsPermissionGrantedUseCase
import com.foryouandme.entity.permission.Permission
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SendDeviceInfoUseCase @Inject constructor(
    private val repository: DeviceRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase
) {

    suspend operator fun invoke() {

        coroutineScope {

            repository.trackDeviceInfo(isPermissionGrantedUseCase(Permission.Location))

            repository.deleteDeviceInfoOlderThan(getTimestampDateUTC().minusDays(5))

            val token = getTokenUseCase.safe()

            if (token != null) {

                val deviceInfo = repository.getDeviceInfo()

                deviceInfo.map {
                    async {
                        catchToNullSuspend {
                            repository.sendDeviceInfo(
                                token,
                                it
                            )
                            repository.deleteDeviceInfo(it.timestamp)
                        }
                    }
                }.awaitAll()
            }

        }
    }

}