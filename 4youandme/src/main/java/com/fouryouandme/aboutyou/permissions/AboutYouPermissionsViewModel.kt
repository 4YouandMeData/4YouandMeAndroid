package com.fouryouandme.aboutyou.permissions

import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.deps.ImageConfiguration
import com.fouryouandme.core.arch.deps.modules.PermissionModule
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.permissionSettingsDialogAction
import com.fouryouandme.core.cases.permission.Permission
import com.fouryouandme.core.cases.permission.PermissionUseCase.isPermissionGranted
import com.fouryouandme.core.cases.permission.PermissionUseCase.requestPermission
import com.fouryouandme.core.entity.configuration.Configuration

class AboutYouPermissionsViewModel(
    navigator: Navigator,
    private val permissionModule: PermissionModule
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

        permissionModule.requestPermission(permissionsItem.permission) {

            navigator.performAction(
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

        }
        initialize(configuration, imageConfiguration)

    }

}