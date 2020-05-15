package org.fouryouandme.web

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.Theme

data class WebState(val configuration: Option<Configuration> = None)

sealed class WebStateUpdate {
    data class Initialization(val configuration: Configuration): WebStateUpdate()
}

sealed class WebLoading {
    object Initialization: WebLoading()
}

sealed class WebError {
    object Initialization: WebError()
}