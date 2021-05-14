package com.foryouandme.ui.permissions

import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.permission.Permission
import com.foryouandme.ui.permissions.compose.PermissionsItem

data class PermissionsState(
    val data: LazyData<PermissionsData> = LazyData.Empty
)

data class PermissionsData(
    val permissions: List<PermissionsItem>,
    val configuration: Configuration
)

sealed class PermissionsAction {

    object Initialize : PermissionsAction()
    data class RequestPermissions(val permission: Permission) : PermissionsAction()
    object ScreenViewed : PermissionsAction()

}

sealed class PermissionsEvent {

    object PermissionPermanentlyDenied : PermissionsEvent()

}