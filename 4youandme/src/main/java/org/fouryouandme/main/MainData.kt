package org.fouryouandme.main

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.Theme

data class MainState(
    val configuration: Option<Configuration> = None,
    val selectedPage: Int = R.id.feed_navigation
)

sealed class MainStateUpdate {
    data class Initialization(val configuration: Configuration): MainStateUpdate()
    data class Page(val selectedPage: Int) : MainStateUpdate()
}

sealed class MainLoading {
    object Initialization: MainLoading()
}

sealed class MainError {
    object Initialization: MainError()
}