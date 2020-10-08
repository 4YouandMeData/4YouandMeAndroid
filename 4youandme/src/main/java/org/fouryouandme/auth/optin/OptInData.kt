package org.fouryouandme.auth.optin

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.optins.OptIns

data class OptInState(
    val optIns: OptIns,
    val permissions: Map<Int, Boolean>
)

sealed class OptInStateUpdate {

    data class Initialization(val optIns: OptIns) : OptInStateUpdate()

    data class Permissions(val permissions: Map<Int, Boolean>) : OptInStateUpdate()

}

sealed class OptInLoading {

    object Initialization : OptInLoading()
    object PermissionSet : OptInLoading()

}

sealed class OptInError {

    object Initialization : OptInError()
    object PermissionSet : OptInError()

}

/* --- navigation --- */

data class OptInWelcomeToOptInPermission(val index: Int) : NavigationAction
data class OptInPermissionToOptInPermission(val index: Int) : NavigationAction
object OptInPermissionToOptInSuccess : NavigationAction
object OptInToConsentUser : NavigationAction