package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.auth.AuthFragmentDirections
import com.foryouandme.auth.onboarding.step.integration.login.IntegrationLoginFragmentDirections
import com.foryouandme.auth.onboarding.step.integration.page.IntegrationPageFragmentDirections
import com.foryouandme.auth.onboarding.step.integration.welcome.IntegrationWelcomeFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

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

