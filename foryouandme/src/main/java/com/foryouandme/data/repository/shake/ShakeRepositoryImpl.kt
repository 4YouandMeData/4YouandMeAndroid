package com.foryouandme.data.repository.shake

import android.hardware.Sensor
import android.hardware.SensorManager
import com.foryouandme.domain.usecase.shake.ShakeRepository
import com.foryouandme.entity.sensor.Shake
import com.foryouandme.entity.sensor.ShakeSensitivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShakeRepositoryImpl @Inject constructor(
    private val sensorManager: SensorManager,
    private val shakeListener: ShakeListener
) : ShakeRepository {

    var isStarted: Boolean = false

    override suspend fun startShakeTracking(shakeSensitivity: ShakeSensitivity): Flow<Shake> {

        if (isStarted.not()) startShakeListener()

        return shakeListener.shake.filter {
            when (shakeSensitivity) {
                ShakeSensitivity.Hard ->
                    it.sensitivity == ShakeSensitivity.Hard
                ShakeSensitivity.Medium ->
                    it.sensitivity == ShakeSensitivity.Hard ||
                            it.sensitivity == ShakeSensitivity.Medium
                else -> true
            }
        }

    }

    private fun startShakeListener() {

        sensorManager.registerListener(
            shakeListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        isStarted = true

    }

    override suspend fun stopShakeTracking() {

        if (shakeListener.subscriptionCount <= 1) {
            sensorManager.unregisterListener(shakeListener)
            isStarted = false
        }

    }

}