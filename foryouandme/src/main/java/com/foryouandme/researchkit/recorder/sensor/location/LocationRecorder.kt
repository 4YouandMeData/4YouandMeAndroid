package com.foryouandme.researchkit.recorder.sensor.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import com.foryouandme.core.ext.*
import com.foryouandme.researchkit.recorder.sensor.json.JsonArrayDataRecorder
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.step.Step
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
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

    private var locationManager: LocationManager? = null

    private var totalDistance = 0.0
    private var firstLocation: Location? = null
    private var lastLocation: Location? = null

    override suspend fun start(context: Context) {

        // reset location data
        firstLocation = null
        lastLocation = null
        totalDistance = 0.0

        // initialize location manager
        if (locationManager == null)
            locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // check if gps is enabled
        val isGpsProviderEnabled =
            locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

        if (!isGpsProviderEnabled) {
            val errorMsg = "The app needs GPS enabled to record accurate location-based data"
            throw IllegalStateException(errorMsg)
        }

        // In-app permissions were added in Android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val pm = context.packageManager

            val hasPerm =
                pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.packageName)

            if (hasPerm != PackageManager.PERMISSION_GRANTED) {
                val errorMsg =
                    "The app needs location permissions to show accurate location-based data"
                throw IllegalStateException(errorMsg)
            }
        }

        // In Android, you can register for both network and gps location updates
        // Let's just register for both and log all locations to the data file
        // with their corresponding accuracy and other data associated
        //startLocationTracking().unsafeRunAsync()
        startLocationTracking()

        super.start(context)

    }

    @SuppressLint("MissingPermission")
    private suspend fun startLocationTracking() {
        withContext(Dispatchers.Main) {
            catch(
                {
                    locationManager?.let {

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
                },
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
                },
            )
        }
    }

    private fun stopLocationListener() {

        catch(
            { locationManager?.removeUpdates(this) },
            { Timber.tag(TAG).e(it, "fail to remove location listener") }
        )

    }

    override suspend fun stop(): FileResult? {

        stopLocationListener()

        return super.stop()

    }

    // locationListener methods
    override fun onLocationChanged(location: Location) {

        GlobalScope.launchSafe { recordLocationData(location) }

    }

    private suspend fun recordLocationData(location: Location) {
        withContext(Dispatchers.IO) {

            // Initialize first location
            if (firstLocation == null) firstLocation = location


            // GPS coordinates

            // Subtract from the firstLocation to get relative coordinates.
            val relativeLatitude =
                firstLocation?.let { location.latitude - it.latitude } ?: 0.toDouble()
            val relativeLongitude =
                firstLocation?.let { location.longitude - it.longitude } ?: 0.toDouble()

            lastLocation?.let { totalDistance += it.distanceTo(location).toDouble() }

            val data =
                LocationRecorderData(
                    getCurrentRecordingTime() ?: 0,
                    LocationRecorderCoordinate(location.latitude, location.longitude),
                    LocationRecorderCoordinate(relativeLatitude, relativeLongitude),
                    location.accuracy,
                    location.speed,
                    location.altitude,
                    location.bearing,
                    totalDistance
                )

            val json = moshi.adapter(LocationRecorderData::class.java).toJson(data)

            onRecordDataCollected(data)

            writeJsonObjectToFile(json)

            lastLocation = location

        }
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

        const val LOCATION_IDENTIFIER: String = "location"

    }
}