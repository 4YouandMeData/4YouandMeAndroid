package com.foryouandme.domain.usecase.shake

import com.foryouandme.entity.sensor.Shake
import com.foryouandme.entity.sensor.ShakeSensitivity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartShakeTrackingUseCase @Inject constructor(
    private val repository: ShakeRepository
) {

    suspend operator fun invoke(shakeSensitivity: ShakeSensitivity): Flow<Shake> =
        repository.startShakeTracking(shakeSensitivity)

}