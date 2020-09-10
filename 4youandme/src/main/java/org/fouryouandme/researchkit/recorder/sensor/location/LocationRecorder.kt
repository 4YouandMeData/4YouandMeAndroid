package org.fouryouandme.researchkit.recorder.sensor.location

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
import arrow.core.getOrElse
import arrow.core.some
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.ext.getOrFalse
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.researchkit.recorder.sensor.json.JsonArrayDataRecorder
import org.fouryouandme.researchkit.step.Step
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
    private val moshi: Moshi,
    private val minTime: Long,
    private val minDistance: Float,
    private val usesRelativeCoordinates: Boolean,
) : JsonArrayDataRecorder(
    identifier,
    step,
    outputDirectory,
), LocationListener {

    private var locationManager: Option<LocationManager> = None

    private var totalDistance = 0.0
    private var firstLocation: Option<Location> = None
    private var lastLocation: Option<Location> = None

    override fun start(context: Context) {
        super.start(context)

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
        startLocationTracking().unsafeRunAsync()
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
                        it.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
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
        stopLocationListener().unsafeRunAsync()
    }

    // locationListener methods
    override fun onLocationChanged(location: Location) {

        recordLocationData(location).unsafeRunAsync()

    }

    private fun recordLocationData(location: Location): IO<Unit> =
        IO.fx {

            continueOn(Dispatchers.IO)

            // Initialize first location
            if (firstLocation.isEmpty()) firstLocation = location.some()


            // GPS coordinates

            // Subtract from the firstLocation to get relative coordinates.
            val relativeLatitude =
                firstLocation.map { location.latitude - it.latitude }.getOrElse { 0.toDouble() }
            val relativeLongitude =
                firstLocation.map { location.longitude - it.longitude }.getOrElse { 0.toDouble() }

            lastLocation.map { totalDistance += it.distanceTo(location).toDouble() }

            val data =
                LocationRecorderData(
                    getCurrentRecordingTime().getOrElse { 0 },
                    LocationRecorderCoordinate(location.latitude, location.longitude),
                    LocationRecorderCoordinate(relativeLatitude, relativeLongitude),
                    location.accuracy,
                    location.speed,
                    location.altitude,
                    location.bearing,
                    totalDistance
                )

            val json = moshi.adapter(LocationRecorderData::class.java).toJson(data)

            !onRecordDataCollected(data)

            !writeJsonObjectToFile(json)

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

    }
}