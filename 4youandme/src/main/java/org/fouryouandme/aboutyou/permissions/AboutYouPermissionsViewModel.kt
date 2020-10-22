package org.fouryouandme.aboutyou.permissions

import android.Manifest
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.PermissionModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.permission.PermissionUseCase.isPermissionGranted
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
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    "The BUMP app needs access to your phone's location",
                    imageConfiguration.location(),
                    permissionModule.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
                )
            )

        setState(AboutYouPermissionsState(permissions))
        { AboutYouPermissionsStateUpdate.Initialization(it.permissions) }

        hideLoading(AboutYouPermissionsLoading.Initialization)

    }

}