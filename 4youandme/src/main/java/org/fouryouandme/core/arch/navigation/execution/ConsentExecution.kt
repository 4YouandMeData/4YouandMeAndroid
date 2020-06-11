package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.consent.page.ConsentPageFragmentDirections
import org.fouryouandme.auth.consent.welcome.ConsentWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentWelcomeToConsentPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentWelcomeFragmentDirections.actionConsentWelcomeToConsentPage(id))
    }

fun consentPageToConsentPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentPageFragmentDirections.actionConsentPageSelf(id))
    }