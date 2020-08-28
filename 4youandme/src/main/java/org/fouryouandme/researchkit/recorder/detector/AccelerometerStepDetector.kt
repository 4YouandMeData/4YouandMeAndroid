package org.fouryouandme.researchkit.recorder.detector

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import timber.log.Timber

/**
 *
 * https://github.com/bagilevi/android-pedometer
 * Accelerometer to Step Algorithm from link is distributed under a No restrictions license
 * TODO: develop a better step detector method, this one has too many unknown constants
 * TODO: it also seems to have a variance of about +-15% in my experiments
 */
class AccelerometerStepDetector {

    private var onStepTakenListener: Option<() -> Unit> = None
    private var mLimit = 10f
    private val mLastValues = FloatArray(3 * 2)
    private val mScale = FloatArray(2)
    private val mYOffset: Float
    private val mLastDirections = FloatArray(3 * 2)
    private val mLastExtremes =
        arrayOf(FloatArray(3 * 2), FloatArray(3 * 2))
    private val mLastDiff = FloatArray(3 * 2)
    private var mLastMatch = -1

    fun setSensitivity(sensitivity: Float) {
        mLimit = sensitivity // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }

    fun processAccelerometerData(sensorEvent: SensorEvent): Unit {

        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            var vSum = 0f
            for (i in 0..2) {
                val v = mYOffset + sensorEvent.values[i] * mScale[1]
                vSum += v
            }
            val k = 0
            val v = vSum / 3
            val direction =
                (if (v > mLastValues[k]) 1 else if (v < mLastValues[k]) -1 else 0).toFloat()
            if (direction == -mLastDirections[k]) {
                // Direction changed
                val extType = if (direction > 0) 0 else 1 // minumum or maximum?
                mLastExtremes[extType][k] = mLastValues[k]
                val diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k])
                if (diff > mLimit) {
                    val isAlmostAsLargeAsPrevious = diff > mLastDiff[k] * 2 / 3
                    val isPreviousLargeEnough = mLastDiff[k] > diff / 3
                    val isNotContra = mLastMatch != 1 - extType
                    mLastMatch =
                        if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                            Timber.tag(TAG).d("step")
                            onStepTaken()
                            extType
                        } else {
                            -1
                        }
                }
                mLastDiff[k] = diff
            }
            mLastDirections[k] = direction
            mLastValues[k] = v
        }
    }

    private fun onStepTaken(): Unit {
        onStepTakenListener.map { it() }
    }

    fun setOnStepTakenListener(listener: () -> Unit): Unit {
        onStepTakenListener = listener.some()
    }

    companion object {

        private const val TAG = "StepDetector"

    }

    init {
        val h = 480 // TODO: remove this constant
        mYOffset = h * 0.5f
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)))
        mScale[1] = -(h * 0.5f * (1.0f / SensorManager.MAGNETIC_FIELD_EARTH_MAX))
    }
}