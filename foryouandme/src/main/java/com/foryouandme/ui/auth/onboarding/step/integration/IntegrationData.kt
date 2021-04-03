package com.foryouandme.ui.auth.onboarding.step.integration


import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.integration.Integration
import com.foryouandme.entity.integration.IntegrationApp

data class IntegrationState(
    val integration: Integration? = null,
    val cookies: Map<String, String> = emptyMap()
)

sealed class IntegrationStateUpdate {

    object Integration : IntegrationStateUpdate()
    object Cookies : IntegrationStateUpdate()

}

sealed class IntegrationLoading {

    object Integration : IntegrationLoading()

}

sealed class IntegrationError {

    object Integration : IntegrationError()

}

sealed class IntegrationStateEvent {

    object GetIntegration : IntegrationStateEvent()
    object ScreenViewed : IntegrationStateEvent()

}

/* --- special action --- */

sealed class SpecialLinkAction {
    data class OpenApp(val app: IntegrationApp) : SpecialLinkAction()
    data class Download(val app: IntegrationApp) : SpecialLinkAction()
}

/* --- navigation --- */

data class IntegrationWelcomeToIntegrationPage(val pageId: String) : NavigationAction
data class IntegrationWelcomeToIntegrationLogin(
    val url: String,
    val nextPage: String?
) : NavigationAction

object IntegrationWelcomeToIntegrationSuccess : NavigationAction

data class IntegrationPageToIntegrationPage(val pageId: String) : NavigationAction
object IntegrationPageToIntegrationSuccess : NavigationAction
data class IntegrationPageToIntegrationLogin(
    val url: String,
    val nextPage: String?
) : NavigationAction

data class IntegrationLoginToIntegrationPage(val id: String) : NavigationAction
object IntegrationLoginToIntegrationSuccess : NavigationAction
