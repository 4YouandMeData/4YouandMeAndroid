package com.foryouandme.data.repository.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.annotation.RequiresPermission
import com.foryouandme.domain.usecase.device.DeviceRepository
import com.foryouandme.entity.device.DeviceLocation
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.ZoneId
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeviceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : DeviceRepository {

    override suspend fun getCurrentBatteryLevel(): Float? {

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter) ?: return null

        return getBatteryPercent(batteryStatus)

    }

    private fun getBatteryPercent(batteryStatus: Intent): Float {

        val level: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale: Int = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return level * 100 / scale.toFloat()

    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_FINE_LOCATION"])
    override suspend fun getLastKnownLocation(): DeviceLocation? =
        suspendCoroutine { continuation ->

            var isResumed = false

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    if (isResumed.not()) {
                        val location = it?.let { DeviceLocation(it.latitude, it.longitude) }
                        continuation.resume(location)
                        isResumed = true
                    }
                }
                .addOnCanceledListener {
                    if (isResumed.not()) {
                        continuation.resume(null)
                        isResumed = true
                    }
                }
                .addOnFailureListener {
                    if (isResumed.not()) {
                        continuation.resume(null)
                        isResumed = true
                    }
                }

        }

    override suspend fun getTimeZone(): String = ZoneId.systemDefault().id

    override suspend fun getHashedSSID(): String {

        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        return wifiInfo.ssid.replace("\"", "")

    }

}