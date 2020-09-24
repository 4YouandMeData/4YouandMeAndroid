package org.fouryouandme.researchkit.recorder.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import arrow.core.toOption
import org.fouryouandme.core.ext.evalOnIO
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.recorder.sensor.json.JsonArrayDataRecorder
import org.fouryouandme.researchkit.step.Step
import timber.log.Timber
import java.io.File

/**
 *
 * The SensorRecorder is an abstract class that greatly reduces the amount of work required
 * to write sensor data to json file
 *
 * Any Android sensor is compatible with this class as long as you correctly implement
 * the two abstract methods, getSensorTypeList, and writeJsonData
 *
 * @property frequency
 * The frequency of the sensor data collection in samples per second (Hz).
 * Android Sensors do not allow exact frequency specifications, per their documentation,
 * it is only a HINT, so we must manage it ourselves with delay
 *
 */
abstract class SensorRecorder(
    thread: String,
    private val frequency: Double,
    identifier: String,
    step: Step,
    outputDirectory: File,
) : JsonArrayDataRecorder(
    identifier,
    step,
    outputDirectory
), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var sensorList: MutableList<Sensor> = mutableListOf()
    private var timestampZeroReferenceNanos: Long = 0

    private val handler = HandlerThread(thread)

    private val sensorHandler by lazy { Handler(handler.looper) }

    init {

        handler.start()

    }

    /**
     * @param  availableSensorList the list of available sensors for the user's device
     * @return a list of sensor types that should be listened to
     * for example, if you only want accelerometer, you would return
     * Collections.singletonList(Sensor.TYPE_ACCELEROMETER)
     */
    protected abstract fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int>

    override suspend fun start(context: Context): Unit {

        super.start(context)

        startSensorRecording(context)
    }

    private suspend fun startSensorRecording(context: Context): Unit {
        evalOnIO {
            sensorManager =
                (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)

            var anySucceeded = false

            sensorManager?.let { sm ->

                val availableSensorList = sm.getSensorList(Sensor.TYPE_ALL)

                sensorList = getSensorTypeList(availableSensorList).mapNotNull { sensorType ->

                    sm.getDefaultSensor(sensorType).toOption()
                        .map {

                            val success =
                                if (isManualFrequency)
                                    sm.registerListener(
                                        this,
                                        it,
                                        SensorManager.SENSOR_DELAY_FASTEST,
                                        sensorHandler
                                    )
                                else
                                    sm.registerListener(
                                        this,
                                        it,
                                        calculateDelayBetweenSamplesInMicroSeconds(),
                                        sensorHandler
                                    )

                            anySucceeded = anySucceeded or success

                            if (success.not())
                                Timber.e("Failed to register sensor: $it")

                            it

                        }.orNull()

                }.toMutableList()
            }

            if (!anySucceeded) super.onRecorderFailed("Failed to initialize any sensor")
            else super.startJsonDataLogging()

        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent): Unit {

        startCoroutineAsync {
            recordSensorEvent(sensorEvent)?.let { writeJsonObjectToFile(it) }
        }
    }

    /***
     * This method receives a SensorEvent and is expected to receive a RecorderData json.
     * @param sensorEvent
     */
    abstract suspend fun recordSensorEvent(sensorEvent: SensorEvent): String?

    override suspend fun stop() {
        super.stop()

        if (handler.isAlive) handler.quitSafely()

        sensorList.forEach { sensor ->
            sensorManager?.unregisterListener(this, sensor)
        }

    }

    override suspend fun cancel() {
        super.cancel()

        if (handler.isAlive) handler.quitSafely()

        sensorList.forEach { sensor ->
            sensorManager?.unregisterListener(this, sensor)
        }
    }

    /**
     * @param availableSensorList the list of available sensors
     * @param sensorType the sensor type to check if it is contained in the list
     * @return true if that sensor type is available, false if it is not
     */
    protected fun hasAvailableType(
        availableSensorList: List<Sensor>,
        sensorType: Int
    ): Boolean {

        for (sensor in availableSensorList) {
            if (sensor.type == sensorType) {
                return true
            }
        }
        return false
    }

    private fun calculateDelayBetweenSamplesInMicroSeconds(): Int =
        (MICRO_SECONDS_PER_SEC.toFloat() / frequency).toInt()

    /**
     * @return
     * true if sensor frequency does not exist, and callbacks will be based on an event,
     * like Step Detection.
     * false if the sensor frequency will come back at a desired frequency.
     */
    private val isManualFrequency: Boolean = frequency < 0

    companion object {

        const val MANUAL_JSON_FREQUENCY = -1.0f
        private const val MICRO_SECONDS_PER_SEC = 1000000L

    }
}