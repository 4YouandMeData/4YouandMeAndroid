package com.foryouandme.ui.aboutyou.permissions

import androidx.annotation.DrawableRes
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.permission.Permission

data class AboutYouPermissionsState(
    val data: LazyData<AboutYouPermissionsData> = LazyData.Empty
)

data class AboutYouPermissionsData(
    val permissions: List<PermissionsItem>,
    val configuration: Configuration
)

data class PermissionsItem(
    val configuration: Configuration,
    val id: String,
    val permission: Permission,
    val description: String,
    @DrawableRes val image: Int,
    val isAllowed: Boolean
)

sealed class AboutYouPermissionsStateUpdate {

    data class Initialization(
        val permissions: List<PermissionsItemOld>
    ) : AboutYouPermissionsStateUpdate()

}

sealed class AboutYouPermissionsLoading {

    object Initialization : AboutYouPermissionsLoading()

}

sealed class AboutYouPermissionsAction {

    object Initialize: AboutYouPermissionsAction()
    data class RequestPermissions(val permission: Permission): AboutYouPermissionsAction()
    object ScreenViewed: AboutYouPermissionsAction()

}

sealed class AboutYouPermissionsEvent {

    object PermissionPermanentlyDenied: AboutYouPermissionsEvent()

}