package org.fouryouandme.auth.optin

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns

data class OptInState(
    val configuration: Option<Configuration> = None,
    val optIns: Option<OptIns> = None,
    val permissions: Map<Int, Boolean> = emptyMap()
)

sealed class OptInStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val optIns: OptIns
    ) : OptInStateUpdate()

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