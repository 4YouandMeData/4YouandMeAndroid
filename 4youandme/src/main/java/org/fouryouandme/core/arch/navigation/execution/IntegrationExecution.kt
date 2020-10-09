package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.AuthFragmentDirections
import org.fouryouandme.auth.integration.login.IntegrationLoginFragmentDirections
import org.fouryouandme.auth.integration.page.IntegrationPageFragmentDirections
import org.fouryouandme.auth.integration.welcome.IntegrationWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun integrationWelcomeToIntegrationPage(id: String): NavigationExecution = {
    it.navigate(IntegrationWelcomeFragmentDirections.actionIntegrationWelcomeToIntegrationPage(id))
}

fun integrationWelcomeToIntegrationLogin(
    url: String,
    nextPageId: String?
): NavigationExecution = {
    it.navigate(
        IntegrationWelcomeFragmentDirections.actionIntegrationWelcomeToIntegrationLogin(
            url,
            nextPageId
        )
    )
}

fun integrationWelcomeToIntegrationSuccess(): NavigationExecution = {
    it.navigate(IntegrationWelcomeFragmentDirections.actionIntegrationWelcomeToIntegrationSuccess())
}

fun integrationPageToIntegrationPage(id: String): NavigationExecution = {
    it.navigate(IntegrationPageFragmentDirections.actionIntegrationPageSelf(id))
}

fun integrationPageToIntegrationLogin(
    url: String,
    nextPageId: String?
): NavigationExecution = {
    it.navigate(
        IntegrationPageFragmentDirections.actionIntegrationPageToIntegrationLogin(
            url,
            nextPageId
        )
    )
}

fun integrationPageToIntegrationSuccess(): NavigationExecution = {
    it.navigate(IntegrationPageFragmentDirections.actionIntegrationPageToIntegrationSuccess())
}

fun integrationLoginToIntegrationPage(id: String): NavigationExecution = {
    it.navigate(IntegrationLoginFragmentDirections.actionIntegrationLoginToIntegrationPage(id))
}

fun integrationLoginToIntegrationSuccess(): NavigationExecution = {
    it.navigate(IntegrationLoginFragmentDirections.actionIntegrationLoginToIntegrationSuccess())
}

fun integrationSuccessToMain(): NavigationExecution = {
    it.navigate(AuthFragmentDirections.actionAuthToMain())
}