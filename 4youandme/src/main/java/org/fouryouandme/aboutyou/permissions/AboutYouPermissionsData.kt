package org.fouryouandme.aboutyou.permissions

data class AboutYouPermissionsState(
    val permissions: List<PermissionsItem>
)

sealed class AboutYouPermissionsStateUpdate {

    data class Initialization(
        val permissions: List<PermissionsItem>
    ) : AboutYouPermissionsStateUpdate()

}

sealed class AboutYouPermissionsLoading {

    object Initialization : AboutYouPermissionsLoading()

}