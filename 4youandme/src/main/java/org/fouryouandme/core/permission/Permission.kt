package org.fouryouandme.core.permission

import android.Manifest
import android.content.Context
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: use use case
sealed class Permission(val name: String) {

    object Camera : Permission(Manifest.permission.CAMERA)
    object RecordAudio : Permission(Manifest.permission.RECORD_AUDIO)

}

sealed class PermissionError {

    data class PermissionDenied(val permission: Permission) : PermissionError()
    object Unknown : PermissionError()
}

suspend fun requestMultiplePermission(
    context: Context,
    vararg permission: Permission
): Either<PermissionError, Unit> {

    return suspendCoroutine { continuation ->

        val permissionStrings = permission.map { it.name }.toTypedArray()

        Dexter.withContext(context)
            .withPermissions(*permissionStrings)
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report != null) {

                        if (report.areAllPermissionsGranted().not()) {

                            val deniedPermission =
                                report.deniedPermissionResponses
                                    .firstOrNull()
                                    ?.permissionName

                            val error =
                                when (deniedPermission) {
                                    Manifest.permission.CAMERA ->
                                        PermissionError.PermissionDenied(Permission.Camera)
                                    Manifest.permission.RECORD_AUDIO ->
                                        PermissionError.PermissionDenied(Permission.RecordAudio)
                                    else ->
                                        PermissionError.Unknown
                                }

                            continuation.resume(error.left())

                        } else continuation.resume(Unit.right())

                    } else continuation.resume(PermissionError.Unknown.left())
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .check()
    }

}