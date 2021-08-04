package com.foryouandme.ui.permissions

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.user.User
import com.foryouandme.ui.permissions.compose.PermissionItem

data class PermissionsState(
    val data: LazyData<PermissionsData> = LazyData.Empty
) {

    companion object {

        fun mock(): PermissionsState =
            PermissionsState(data = PermissionsData.mock().toData())

    }

}

data class PermissionsData(
    val user: User,
    val permissions: List<PermissionItem>,
    val configuration: Configuration
) {

    companion object {

        fun mock(): PermissionsData =
            PermissionsData(
                user = User.mock(),
                permissions = listOf(PermissionItem.mock()),
                configuration = Configuration.mock()
            )

    }

}

sealed class PermissionsAction {

    object Initialize : PermissionsAction()
    data class RequestPermissions(val permission: Permission) : PermissionsAction()
    object ScreenViewed : PermissionsAction()

}

sealed class PermissionsEvent {

    object PermissionPermanentlyDenied : PermissionsEvent()

}