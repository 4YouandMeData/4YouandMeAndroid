package com.foryouandme.data.repository.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.foryouandme.domain.usecase.permission.PermissionRepository
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermissionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PermissionRepository {

    override fun isPermissionGranted(permission: Permission): Boolean =
        if (Build.VERSION.SDK_INT >= 23)
            ContextCompat.checkSelfPermission(context, permission.name).isGranted()
        else
            PermissionChecker.checkSelfPermission(context, permission.name).isGranted()

    private fun Int.isGranted(): Boolean = this == PackageManager.PERMISSION_GRANTED

    override suspend fun requestPermission(permission: Permission): PermissionResult =
        suspendCoroutine {

            Dexter.withContext(context)
                .withPermission(permission.name)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                        it.resume(PermissionResult.Granted(permission))

                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

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

    override suspend fun requestPermissions(permissions: List<Permission>): List<PermissionResult> =
        suspendCoroutine { continuation ->

            Dexter.withContext(context)
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