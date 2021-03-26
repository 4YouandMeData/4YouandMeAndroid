package com.foryouandme.ui.aboutyou.permissions

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.PermissionModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.action.permissionSettingsDialogAction
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.permission.Permission
import com.foryouandme.core.cases.permission.PermissionResult
import com.foryouandme.core.cases.permission.PermissionUseCase.isPermissionGranted
import com.foryouandme.core.cases.permission.PermissionUseCase.requestPermission
import com.foryouandme.entity.configuration.Configuration

class AboutYouPermissionsViewModel(
    navigator: Navigator,
    private val permissionModule: PermissionModule,
    private val analyticsModule: AnalyticsModule
) :
    BaseViewModel<
            AboutYouPermissionsState,
            AboutYouPermissionsStateUpdate,
            Empty,
            AboutYouPermissionsLoading>(navigator) {

    suspend fun initialize(configuration: Configuration, imageConfiguration: ImageConfiguration) {

        showLoading(AboutYouPermissionsLoading.Initialization)

        val permissions =
            listOf(
                PermissionsItem(
                    configuration,
                    "1",
                    Permission.Location,
                    "The BUMP app needs access to your phone's location",
                    imageConfiguration.location(),
                    permissionModule.isPermissionGranted(Permission.Location)
                )
            )

        setState(AboutYouPermissionsState(permissions))
        { AboutYouPermissionsStateUpdate.Initialization(it.permissions) }

        hideLoading(AboutYouPermissionsLoading.Initialization)

    }

    suspend fun requestPermission(
        permissionsItem: PermissionsItem,
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ): Unit {

        val permissionRequest = permissionModule.requestPermission(permissionsItem.permission)

        if(permissionRequest is PermissionResult.Denied && permissionRequest.isPermanentlyDenied)
            navigator.performActionSuspend(
                permissionSettingsDialogAction(
                    navigator,
                    configuration.text.profile.permissionDenied,
                    configuration.text.profile.permissionMessage,
                    configuration.text.profile.permissionSettings,
                    configuration.text.profile.permissionCancel,
                    true,
                    {},
                    {}
                )
            )

        initialize(configuration, imageConfiguration)

    }

    /* --- navigation --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.Permissions, EAnalyticsProvider.ALL)

}