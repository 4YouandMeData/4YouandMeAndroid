package com.foryouandme.domain.usecase.task

import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import javax.inject.Inject

class SendFitnessTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(
        taskId: String,
        walkDeviceMotion: String,
        walkAccelerometer: String,
        walkPedometer: String,
        sitDeviceMotion: String,
        sitAccelerometer: String,
    ) {

        repository.updateFitnessTask(
            getTokenUseCase(),
            taskId,
            walkDeviceMotion,
            walkAccelerometer,
            walkPedometer,
            sitDeviceMotion,
            sitAccelerometer
        )

    }

}