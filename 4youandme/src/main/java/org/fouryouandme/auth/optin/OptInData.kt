package org.fouryouandme.auth.optin

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns

data class OptInState(
    val configuration: Option<Configuration> = None,
    val optIns: Option<OptIns> = None,
    val permissions: Map<String, Boolean> = emptyMap()
)

sealed class OptInStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val optIns: OptIns
    ) : OptInStateUpdate()

    data class Permissions(val permissions: Map<String, Boolean>) : OptInStateUpdate()

}

sealed class OptInLoading {

    object Initialization : OptInLoading()

}

sealed class OptInError {

    object Initialization : OptInError()

}

/* --- navigation --- */

data class OptInWelcomeToOptInPermission(val id: String) : NavigationAction