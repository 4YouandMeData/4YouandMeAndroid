package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.auth.consent.failure.ConsentFailureFragmentDirections
import org.fouryouandme.auth.consent.page.ConsentPageFragmentDirections
import org.fouryouandme.auth.consent.question.ConsentQuestionFragmentDirections
import org.fouryouandme.auth.consent.welcome.ConsentWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentWelcomeToConsentPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentWelcomeFragmentDirections.actionConsentWelcomeToConsentPage(id))
    }

fun consentWelcomeToConsentQuestion(index: Int): NavigationExecution =
    {
        it.navigate(ConsentWelcomeFragmentDirections.actionConsentWelcomeToConsentQuestion(index))
    }

fun consentPageToConsentPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentPageFragmentDirections.actionConsentPageSelf(id))
    }

fun consentPageToConsentQuestion(index: Int): NavigationExecution =
    {
        it.navigate(ConsentPageFragmentDirections.actionConsentPageToConsentQuestion(index))
    }

fun consentQuestionToConsentQuestion(index: Int): NavigationExecution =
    {
        it.navigate(ConsentQuestionFragmentDirections.actionConsentQuestionSelf(index))
    }

fun consentQuestionToConsentSuccess(): NavigationExecution =
    {
        it.navigate(ConsentQuestionFragmentDirections.actionConsentQuestionToConsentSuccess())
    }

fun consentQuestionToConsentFailure(): NavigationExecution =
    {
        it.navigate(ConsentQuestionFragmentDirections.actionConsentQuestionToConsentFailure())
    }

fun consentFailureToConsentWelcome(): NavigationExecution =
    {
        it.popBackStack(R.id.consent_welcome, false)
    }

fun consentFailureToConsentPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentFailureFragmentDirections.actionConsentFailureToConsentPage(id))
    }