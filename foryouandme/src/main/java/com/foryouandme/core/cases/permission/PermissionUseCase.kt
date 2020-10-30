package com.foryouandme.core.cases.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.ext.startCoroutineAsync
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class Permission(val name: String) {

    object Camera : Permission(Manifest.permission.CAMERA)
    object RecordAudio : Permission(Manifest.permission.RECORD_AUDIO)
    object Location : Permission(Manifest.permission.ACCESS_FINE_LOCATION)

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
        onPermanentlyDenied: suspend () -> Unit
    ): Boolean =

        suspendCoroutine {

            Dexter.withContext(application)
                .withPermission(permission.name)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        it.resume(true)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                        if (p0?.isPermanentlyDenied == true)
                            startCoroutineAsync { onPermanentlyDenied() }

                        it.resume(false)

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

}