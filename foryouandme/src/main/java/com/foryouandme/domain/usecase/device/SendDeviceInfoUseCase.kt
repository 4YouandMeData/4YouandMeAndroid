package com.foryouandme.domain.usecase.device

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.entity.device.DeviceInfo
import javax.inject.Inject

class SendDeviceInfoUseCase @Inject constructor(
    private val repository: DeviceRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(deviceInfo: DeviceInfo) {

        repository.sendDeviceInfo(
            getTokenUseCase(),
            deviceInfo
        )

    }

}