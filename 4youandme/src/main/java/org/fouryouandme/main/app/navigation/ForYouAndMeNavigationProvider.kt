package org.fouryouandme.main.app.navigation

import org.fouryouandme.auth.splash.SplashToWelcome
import org.fouryouandme.auth.welcome.WelcomeToSignUpInfo
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.core.arch.navigation.NavigationProvider
import org.fouryouandme.main.app.navigation.execution.splashToWelcome
import org.fouryouandme.main.app.navigation.execution.welcomeToSignUpInfo

class ForYouAndMeNavigationProvider: NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution =
        when (action) {
            is SplashToWelcome -> splashToWelcome()
            is WelcomeToSignUpInfo -> welcomeToSignUpInfo()
            else -> {
                {}
            }
        }
}