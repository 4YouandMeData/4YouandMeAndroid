package org.fouryouandme.auth.welcome

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.Theme

data class WelcomeState(val configuration: Option<Configuration> = None)

sealed class WelcomeStateUpdate {
    data class Initialization(val configuration: Configuration): WelcomeStateUpdate()
}

sealed class WelcomeLoading {
    object Initialization: WelcomeLoading()
}

sealed class WelcomeError {
    object Initialization: WelcomeError()
}