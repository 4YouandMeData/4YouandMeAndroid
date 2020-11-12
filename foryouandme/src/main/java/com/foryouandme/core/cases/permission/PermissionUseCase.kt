package com.foryouandme.core.cases.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.ext.startCoroutineAsync
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class Permission(val name: String) {

    object Camera : Permission(Manifest.permission.CAMERA)
    object RecordAudio : Permission(Manifest.permission.RECORD_AUDIO)
    object Location : Permission(Manifest.permission.ACCESS_FINE_LOCATION)

    companion object {

        fun fromName(name: String): Permission? =
            when (name) {

                Camera.name -> Camera
                RecordAudio.name -> RecordAudio
                Location.name -> Location
                else -> null

            }

    }

}

sealed class PermissionResult(val permission: Permission) {

    class Granted(permission: Permission) : PermissionResult(permission)
    class Denied(
        permission: Permission,
        val isPermanentlyDenied: Boolean
    ) : PermissionResult(permission)

}

object PermissionUseCase {

    suspend fun PermissionModule.isPermissionGranted(permission: Permission): Boolean =
        if (Build.VERSION.SDK_INT >= 23)
            ContextCompat.checkSelfPermission(application, permission.name).isGranted()
        else
            PermissionChecker.checkSelfPermission(application, permission.name).isGranted()


    private fun Int.isGranted(): Boolean = this == PackageManager.PERMISSION_GRANTED

    suspend fun PermissionModule.requestPermission(
        permission: Permission,
    ): PermissionResult =

        suspendCoroutine {

            Dexter.withContext(application)
                .withPermission(permission.name)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                        if (permission is Permission.Location)
                            startCoroutineAsync {
                                analyticsModule.logEvent(
                                    AnalyticsEvent.LocationPermissionChanged(true),
                                    EAnalyticsProvider.ALL
                                )
                            }


                        it.resume(PermissionResult.Granted(permission))
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                        startCoroutineAsync {
                            analyticsModule.logEvent(
                                AnalyticsEvent.LocationPermissionChanged(false),
                                EAnalyticsProvider.ALL
                            )
                        }

                        it.resume(
                            PermissionResult.Denied(
                                permission,
                                p0?.isPermanentlyDenied == true
                            )
                        )

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()

                    }

                }
                )
                .check()
        }

    suspend fun PermissionModule.requestPermissions(
        permissions: List<Permission>
    ): List<PermissionResult> =
        suspendCoroutine { continuation ->

            Dexter.withContext(application)
                .withPermissions(permissions.map { it.name })
                .withListener(
                    object : BaseMultiplePermissionsListener() {

                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {

                            val granted =
                                p0?.grantedPermissionResponses
                                    ?.mapNotNull { Permission.fromName(it.permissionName) }
                                    ?.map { PermissionResult.Granted(it) } ?: emptyList()

                            val denied =
                                p0?.deniedPermissionResponses
                                    ?.mapNotNull { permissionDenied ->
                                        Permission.fromName(permissionDenied.permissionName)
                                            ?.let {
                                                PermissionResult.Denied(
                                                    it,
                                                    permissionDenied.isPermanentlyDenied
                                                )
                                            }
                                    } ?: emptyList()

                            val result = granted.plus(denied)

                            result.firstOrNull { it.permission is Permission.Location }
                                ?.let {

                                    startCoroutineAsync {
                                        analyticsModule.logEvent(
                                            AnalyticsEvent.LocationPermissionChanged(
                                                it is PermissionResult.Granted
                                            ),
                                            EAnalyticsProvider.ALL
                                        )
                                    }

                                }

                            continuation.resume(result)

                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: MutableList<PermissionRequest>?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                        }
                    }

                )
                .check()

        }

}