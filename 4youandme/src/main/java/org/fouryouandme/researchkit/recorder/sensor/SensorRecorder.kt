package org.fouryouandme.researchkit.recorder.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import arrow.core.toOption
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.step.Step
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
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
    /**
     *
     */
    private val frequency: Double,
    identifier: String,
    step: Step,
    outputDirectory: File,
    moshi: Moshi
) : JsonArrayDataRecorder(identifier, step, outputDirectory, moshi), SensorEventListener {

    private var sensorManager: Option<SensorManager> = None
    private var sensorList: MutableList<Sensor> = mutableListOf()
    private var timestampZeroReferenceNanos: Long = 0

    /**
     * @param  availableSensorList the list of available sensors for the user's device
     * @return a list of sensor types that should be listened to
     * for example, if you only want accelerometer, you would return
     * Collections.singletonList(Sensor.TYPE_ACCELEROMETER)
     */
    protected abstract fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int>

    override fun start(context: Context): Unit {

        sensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager).some()

        var anySucceeded = false

        sensorManager.map { sm ->

            val availableSensorList = sm.getSensorList(Sensor.TYPE_ALL)

            sensorList = getSensorTypeList(availableSensorList).mapNotNull { sensorType ->

                sm.getDefaultSensor(sensorType).toOption()
                    .map {

                        val success =
                            if (isManualFrequency)
                                sm.registerListener(
                                    this, it, SensorManager.SENSOR_DELAY_FASTEST
                                )
                            else
                                sm.registerListener(
                                    this, it,
                                    calculateDelayBetweenSamplesInMicroSeconds()
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

    override fun onSensorChanged(sensorEvent: SensorEvent): Unit {

        val json = mutableMapOf<String, Any>()

        if (timestampZeroReferenceNanos <= 0) {

            // set timestamp reference, which timestamps are measured relative to
            timestampZeroReferenceNanos = sensorEvent.timestamp

            // record date equivalent of timestamp reference
            val uptimeNanos: Long = SystemClock.elapsedRealtimeNanos()

            val timestampReferenceMillis =
                (System.currentTimeMillis()
                        + ((timestampZeroReferenceNanos - uptimeNanos) * 1e-6).toLong())

            val timestampReferenceDate =
                Instant.ofEpochMilli(timestampReferenceMillis).atZone(ZoneId.systemDefault())

            json[TIMESTAMP_DATE_KEY] =
                timestampReferenceDate.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }

        // these values are doubles
        json[TIMESTAMP_IN_SECONDS_KEY] =
            (sensorEvent.timestamp - timestampZeroReferenceNanos) * 1e-9

        json[UPTIME_IN_SECONDS_KEY] = sensorEvent.timestamp * 1e-9

        val sensorJson = recordSensorEvent(sensorEvent).plus(json)

        writeJsonObjectToFile(sensorJson)

    }

    /***
     * This method receives a SensorEvent and a json and is expected to update the
     * json with data to be written.
     * @param sensorEvent
     */
    abstract fun recordSensorEvent(sensorEvent: SensorEvent): Map<String, Any>

    override fun stop() {
        super.stop()
        sensorList.forEach { sensor ->
            sensorManager.map { it.unregisterListener(this, sensor) }
        }

    }

    override fun cancel() {
        super.cancel()
        sensorList.forEach { sensor ->
            sensorManager.map { it.unregisterListener(this, sensor) }
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
        const val TIMESTAMP_IN_SECONDS_KEY = "timestamp"
        const val UPTIME_IN_SECONDS_KEY = "uptime"
        const val TIMESTAMP_DATE_KEY = "timestampDate"

    }
}