package com.foryouandme.domain.usecase.shake

import com.foryouandme.entity.sensor.Shake
import com.foryouandme.entity.sensor.ShakeSensitivity
import kotlinx.coroutines.flow.Flow

interface ShakeRepository {

    suspend fun startShakeTracking(shakeSensitivity: ShakeSensitivity): Flow<Shake>

    suspend fun stopShakeTracking()

}