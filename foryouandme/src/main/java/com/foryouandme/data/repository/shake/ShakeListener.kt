package com.foryouandme.data.repository.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.entity.sensor.Shake
import com.foryouandme.entity.sensor.ShakeSensitivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import kotlin.math.sqrt

class ShakeListener @Inject constructor() : SensorEventListener {

    private val shakeFlow = MutableSharedFlow<Shake>()

    val shake: SharedFlow<Shake> = shakeFlow

    val subscriptionCount: Int
        get() = shakeFlow.subscriptionCount.value

    private var lastShakeTimeStamp: Long = 0

    override fun onSensorChanged(event: SensorEvent) {

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        // gForce will be close to 1 when there is no movement.
        // gForce will be close to 1 when there is no movement.
        val gForce: Float = sqrt(gX * gX + gY * gY + gZ * gZ)

        val shake = getShake(gForce)
        if (shake != null) {
            val now = System.currentTimeMillis()
            // ignore shake events too close to each other (500ms)
            if (lastShakeTimeStamp + 500 < now)
                GlobalScope.launchSafe { shakeFlow.emit(shake) }

            lastShakeTimeStamp = now
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun getShake(gForce: Float): Shake? =
        when {
            gForce >= 2.4 -> Shake(ShakeSensitivity.Hard)
            gForce >= 2.7 -> Shake(ShakeSensitivity.Medium)
            gForce >= 3 -> Shake(ShakeSensitivity.Light)
            else -> null
        }

}