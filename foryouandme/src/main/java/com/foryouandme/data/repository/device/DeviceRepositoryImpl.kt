package com.foryouandme.data.repository.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import androidx.annotation.RequiresPermission
import com.foryouandme.core.ext.catchToNullSuspend
import com.foryouandme.data.datasource.database.ForYouAndMeDatabase
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.ext.getTimestampDateUTC
import com.foryouandme.data.repository.device.database.toDatabaseEntity
import com.foryouandme.data.repository.device.network.DeviceApi
import com.foryouandme.data.repository.device.network.request.DeviceInfoDataRequest
import com.foryouandme.data.repository.device.network.request.DeviceInfoRequest
import com.foryouandme.domain.usecase.device.DeviceRepository
import com.foryouandme.entity.device.DeviceInfo
import com.foryouandme.entity.device.DeviceLocation
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.ZoneId
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeviceRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val api: DeviceApi,
    private val authErrorInterceptor: AuthErrorInterceptor,
    private val database: ForYouAndMeDatabase
) : DeviceRepository {

    override suspend fun trackDeviceInfo(isLocationPermissionEnabled: Boolean) {
        coroutineScope {

            val batteryLevel = async { catchToNullSuspend { getCurrentBatteryLevel() } }
            val location =
                async {
                    catchToNullSuspend {
                        if (isLocationPermissionEnabled)
                            getLastKnownLocation()
                        else
                            null
                    }
                }
            val timeZone = async { catchToNullSuspend { getTimeZone() } }
            val hashedSSID = async { catchToNullSuspend { getHashedSSID() } }

            val deviceInfo =
                DeviceInfo(
                    batteryLevel.await(),
                    location.await(),
                    timeZone.await(),
                    hashedSSID.await(),
                    getTimestampDateUTC()
                )

            saveDeviceInfo(deviceInfo)

        }
    }

    private fun getCurrentBatteryLevel(): Float? {

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
    private suspend fun getLastKnownLocation(): DeviceLocation? =
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

    private fun getTimeZone(): String = ZoneId.systemDefault().id

    private fun getHashedSSID(): String? {

        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid.replace("\"", "")

        return ssid.md5()

    }

    private fun String.md5(): String =
        MessageDigest
            .getInstance("MD5")
            .digest(toByteArray())
            .printHexBinary()

    private fun ByteArray.printHexBinary(): String {
        val r = StringBuilder(size * 2)
        forEach { b ->
            val i = b.toInt()
            r.append(this[i shr 4 and 0xF])
            r.append(this[i and 0xF])
        }
        return r.toString()
    }

    override suspend fun saveDeviceInfo(deviceInfo: DeviceInfo) {
        database.deviceInfoDao().insertDeviceInfo(deviceInfo.toDatabaseEntity())
    }

    override suspend fun getDeviceInfo(): List<DeviceInfo> =
        database.deviceInfoDao().getDeviceInfo().map { it.toDeviceInfo() }

    override suspend fun deleteDeviceInfoOlderThan(timestamp: Date) {
        database.deviceInfoDao().deleteDeviceInfoOlderThan(timestamp)
    }

    override suspend fun deleteDeviceInfo(timestamp: Date) {
        database.deviceInfoDao().deleteDeviceInfo(timestamp)
    }

    override suspend fun sendDeviceInfo(token: String, deviceInfo: DeviceInfo) {

        authErrorInterceptor.execute {
            api.sendDeviceInfo(
                token,
                DeviceInfoRequest(
                    DeviceInfoDataRequest(
                        batteryLevel = deviceInfo.batteryLevel,
                        longitude = deviceInfo.location?.longitude,
                        latitude = deviceInfo.location?.latitude,
                        timeZone = deviceInfo.timeZone,
                        hashedSSID = deviceInfo.hashedSSID,
                        timestamp = deviceInfo.timestamp.time
                    )
                )
            )
        }

    }

}