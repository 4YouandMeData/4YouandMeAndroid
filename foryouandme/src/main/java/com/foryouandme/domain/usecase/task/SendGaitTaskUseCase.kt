package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class SendGaitTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        outboundDeviceMotion: String,
        outboundAccelerometer: String,
        outboundPedometer: String,
        returnDeviceMotion: String,
        returnAccelerometer: String,
        returnPedometer: String,
        restDeviceMotion: String,
        restAccelerometer: String,
    ) {

        repository.updateGaitTask(
            getTokenUseCase(),
            taskId,
            outboundDeviceMotion,
            outboundAccelerometer,
            outboundPedometer,
            returnDeviceMotion,
            returnAccelerometer,
            returnPedometer,
            restDeviceMotion,
            restAccelerometer
        )

    }

}