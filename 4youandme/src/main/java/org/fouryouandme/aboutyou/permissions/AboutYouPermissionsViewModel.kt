package org.fouryouandme.aboutyou.permissions

import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.PermissionModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.permission.Permission
import org.fouryouandme.core.cases.permission.PermissionUseCase.isPermissionGranted
import org.fouryouandme.core.cases.permission.PermissionUseCase.requestPermission
import org.fouryouandme.core.entity.configuration.Configuration

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

        permissionModule.requestPermission(permissionsItem.permission)
        initialize(configuration, imageConfiguration)

    }

}