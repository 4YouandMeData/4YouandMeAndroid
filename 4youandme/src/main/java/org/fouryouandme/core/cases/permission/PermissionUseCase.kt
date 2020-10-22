package org.fouryouandme.core.cases.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import org.fouryouandme.core.arch.deps.modules.PermissionModule

object PermissionUseCase {

    suspend fun PermissionModule.isPermissionGranted(permission: String): Boolean =
        if (Build.VERSION.SDK_INT >= 23)
            ContextCompat.checkSelfPermission(application, permission).isGranted()
        else
            PermissionChecker.checkSelfPermission(application, permission).isGranted()


    private fun Int.isGranted(): Boolean = this == PackageManager.PERMISSION_GRANTED

}