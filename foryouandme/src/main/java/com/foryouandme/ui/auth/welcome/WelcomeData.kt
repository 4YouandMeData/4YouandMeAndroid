package com.foryouandme.ui.auth.welcome

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class WelcomeState(
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class WelcomeAction {
    object ScreenViewed : WelcomeAction()
    object GetConfiguration: WelcomeAction()
}

/* --- navigation --- */

object WelcomeToSignUpInfo : NavigationAction