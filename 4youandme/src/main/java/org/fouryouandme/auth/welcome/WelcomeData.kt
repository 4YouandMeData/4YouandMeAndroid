package org.fouryouandme.auth.welcome

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.theme.Theme

data class WelcomeState(val theme: Option<Theme> = None)

sealed class WelcomeStateUpdate {
    data class Initialization(val theme: Theme): WelcomeStateUpdate()
}

sealed class WelcomeLoading {
    object Initialization: WelcomeLoading()
}

sealed class WelcomeError {
    object Initialization: WelcomeError()
}