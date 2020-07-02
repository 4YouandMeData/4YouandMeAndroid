package org.fouryouandme.auth.optin

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns

data class OptInState(
    val configuration: Option<Configuration> = None,
    val optIns: Option<OptIns> = None
)

sealed class OptInStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val optIns: OptIns
    ) : OptInStateUpdate()

}

sealed class OptInLoading {

    object Initialization : OptInLoading()

}

sealed class OptInError {

    object Initialization : OptInError()

}

/* --- navigation --- */

data class OptInWelcomeToOptInPermission(val id: String) : NavigationAction