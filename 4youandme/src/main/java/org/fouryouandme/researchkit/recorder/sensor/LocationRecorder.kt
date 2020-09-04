package org.fouryouandme.researchkit.recorder.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import arrow.core.None
import arrow.core.Option
import arrow.core.some
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.ext.getOrFalse
import org.fouryouandme.researchkit.step.Step
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.io.File

/**
 * @property identifier the recorder's identifier
 * @property step the step that contains this recorder
 * @property outputDirectory the output directory of the file that will be written
 * with location data
 * @property moshi instance used to encrypt the data
 * @property minTime minimum time interval between location updates, in milliseconds
 * @property minDistance minimum distance between location updates, in meters, no minimum if zero
 * @property usesRelativeCoordinates If this is set to true, the recorder will produce relative
 * GPS coordinates, using the user's initial position as zero in the relative coordinate system.
 * If this is set to false, the recorder will produce absolute GPS coordinates.
 */
open class LocationRecorder(
    identifier: String,
    step: Step,
    outputDirectory: File,
    moshi: Moshi,
    private val minTime: Long,
    private val minDistance: Float,
    private val usesRelativeCoordinates: Boolean,
) : JsonArrayDataRecorder(identifier, step, outputDirectory, moshi), LocationListener {

    private var locationManager: Option<LocationManager> = None

    private var totalDistance = 0.0
    private var firstLocation: Option<Location> = None
    private var lastLocation: Option<Location> = None
    private var startTimeNanosSinceBoot: Long = 0

    override fun start(context: Context) {

        // reset location data
        firstLocation = None
        lastLocation = None
        totalDistance = 0.0

        // initialize location manager
        if (locationManager.isEmpty())
            locationManager =
                (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).some()


        // check if gps is enabled
        val isGpsProviderEnabled =
            locationManager.map { it.isProviderEnabled(LocationManager.GPS_PROVIDER) }.getOrFalse()

        if (!isGpsProviderEnabled) {

            recorderListener.map {
                val errorMsg =
                    "The app needs GPS enabled to record accurate location-based data"
                it.onFail(this, IllegalStateException(errorMsg))
            }

            return
        }

        // In-app permissions were added in Android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val pm = context.packageManager

            val hasPerm =
                pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.packageName)

            if (hasPerm != PackageManager.PERMISSION_GRANTED) {

                recorderListener.map {
                    val errorMsg =
                        "The app needs location permissions to show accurate location-based data"
                    it.onFail(this, IllegalStateException(errorMsg))
                }

                return
            }
        }

        // In Android, you can register for both network and gps location updates
        // Let's just register for both and log all locations to the data file
        // with their corresponding accuracy and other data associated
        //startLocationTracking().unsafeRunAsync()
        locationManager.map {

            it.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                this
            )
            it.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                this
            )
        }
        startJsonDataLogging()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking(): IO<Unit> =
        IO.fx {
            continueOn(Dispatchers.Main)
            val start =
                IO.fx {
                    continueOn(Dispatchers.Main)
                    locationManager.map {

                        it.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            minTime,
                            minDistance,
                            this@LocationRecorder
                        )
                    }

                }.attempt().bind()

            start.fold(
                {
                    when (it) {
                        is SecurityException ->
                            Timber.tag(TAG)
                                .e(it, "fail to request location update, ignore")
                        is IllegalArgumentException ->
                            Timber.tag(TAG)
                                .e(it, "gps provider does not exist ${it.message}")
                        else ->
                            Timber.tag(TAG)
                                .e(it, "can't start gps tracking ${it.message}")

                    }

                    Unit
                },
                { Unit }
            )
        }

    private fun stopLocationListener(): IO<Unit> =
        IO.fx {

            val stop =
                IO.fx { locationManager.map { it.removeUpdates(this@LocationRecorder) } }
                    .attempt()
                    .bind()

            stop.mapLeft {
                Timber.tag(TAG)
                    .e(it, "fail to remove location listener")
            }

        }

    override fun stop() {
        super.stop()
        stopLocationListener().unsafeRunSync()
    }

    // locationListener methods
    override fun onLocationChanged(location: Location) {

        // Initialize first location
        if (firstLocation.isEmpty()) firstLocation = location.some()

        // elapsedRealtimeNanos is long nanoseconds since system boot time.
        val locationNanos = location.elapsedRealtimeNanos

        val json = mutableMapOf<String, Any>()
        val coordinateJson = mutableMapOf<String, Any>()

        if (startTimeNanosSinceBoot == 0L) {

            // Initialize start time.
            startTimeNanosSinceBoot = locationNanos

            // Add timestamp date, which is the ISO timestamp representing the activity start time.
            // Location.getTime() is always epoch milliseconds, so we can use as is.
            val timestamp =
                Instant.ofEpochMilli(location.time).atZone(ZoneOffset.UTC)

            json[TIMESTAMP_DATE_KEY] =
                timestamp.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

        }

        // Timestamps
        // timestamp is seconds since start of the activity
        // (locationNanos minus startTimeNanos, divided by a billion).
        // uptime is a monotonically increasing timestamp in seconds, with any arbitrary zero.
        // (We use getElapsedRealtimeNanos(), divided by a billion.)
        json[TIMESTAMP_IN_SECONDS_KEY] = (locationNanos - startTimeNanosSinceBoot) * 1e-9
        json[UPTIME_IN_SECONDS_KEY] = locationNanos * 1e-9

        // GPS coordinates
        if (usesRelativeCoordinates) {
            // Subtract from the firstLocation to get relative coordinates.
            val relativeLatitude =
                firstLocation.map { location.latitude - it.latitude }
            val relativeLongitude =
                firstLocation.map { location.longitude - it.longitude }

            coordinateJson[RELATIVE_LATITUDE_KEY] = relativeLatitude
            coordinateJson[RELATIVE_LONGITUDE_KEY] = relativeLongitude

        } else {

            // Use absolute coordinates given by the location.
            coordinateJson[LONGITUDE_KEY] = location.longitude
            coordinateJson[LATITUDE_KEY] = location.latitude

        }

        json[COORDINATE_KEY] = coordinateJson

        if (location.hasAccuracy())
            json[ACCURACY_KEY] = location.accuracy
        if (location.hasSpeed())
            json[SPEED_KEY] = location.speed
        if (location.hasAltitude())
            json[ALTITUDE_KEY] = location.altitude
        if (location.hasBearing())
            json[COURSE_KEY] = location.bearing

        writeJsonObjectToFile(json)

        lastLocation.map { totalDistance += it.distanceTo(location).toDouble() }

        // TODO: send location
        /*sendLocationUpdateBroadcast(
            location.longitude, location.latitude, totalDistance
        )*/

        lastLocation = location.some()

    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
        Timber.tag(TAG).i(s)
    }

    override fun onProviderEnabled(s: String) {
        Timber.tag(TAG).i("onProviderEnabled $s")
    }

    override fun onProviderDisabled(s: String) {
        Timber.tag(TAG).i("onProviderDisabled $s")
    }

    companion object {

        private val TAG = LocationRecorder::class.java.simpleName

        const val COORDINATE_KEY = "coordinate"
        const val LONGITUDE_KEY = "longitude"
        const val LATITUDE_KEY = "latitude"
        const val ALTITUDE_KEY = "altitude"
        const val ACCURACY_KEY = "accuracy"
        const val COURSE_KEY = "course"
        const val RELATIVE_LATITUDE_KEY = "relativeLatitude"
        const val RELATIVE_LONGITUDE_KEY = "relativeLongitude"
        const val SPEED_KEY = "speed"
        const val TIMESTAMP_DATE_KEY = "timestampDate"
        const val TIMESTAMP_IN_SECONDS_KEY = "timestamp"
        const val UPTIME_IN_SECONDS_KEY = "uptime"

    }
}