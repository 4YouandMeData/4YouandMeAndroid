package org.fouryouandme.auth.integration


import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.integration.Integration

data class IntegrationState(
    val configuration: Option<Configuration> = None,
    val integration: Option<Integration> = None,
    val cookies: Option<Map<String, String>> = None
)

sealed class IntegrationStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val integration: Integration
    ) : IntegrationStateUpdate()

    data class Cookies(val cookies: Map<String, String>) : IntegrationStateUpdate()

}

sealed class IntegrationLoading {

    object Initialization : IntegrationLoading()

}

sealed class IntegrationError {

    object Initialization : IntegrationError()

}


/* --- special action --- */

sealed class App(val packageName: String) {

    object Oura : App("com.ouraring.oura")
    object Fitbit : App("com.fitbit.FitbitMobile")

}

sealed class SpecialLinkAction {
    data class OpenApp(val app: App) : SpecialLinkAction()
    data class Download(val app: App) : SpecialLinkAction()
}

/* --- navigation --- */

data class IntegrationWelcomeToIntegrationPage(val pageId: String) : NavigationAction
data class IntegrationWelcomeToIntegrationLogin(
    val url: String,
    val nextPage: Option<String>
) : NavigationAction

object IntegrationWelcomeToIntegrationSuccess : NavigationAction

data class IntegrationPageToIntegrationPage(val pageId: String) : NavigationAction
object IntegrationPageToIntegrationSuccess : NavigationAction
data class IntegrationPageToIntegrationLogin(
    val url: String,
    val nextPage: Option<String>
) : NavigationAction

data class IntegrationLoginToIntegrationPage(val id: String) : NavigationAction
object IntegrationLoginToIntegrationSuccess : NavigationAction
object IntegrationSuccessToMain : NavigationAction
