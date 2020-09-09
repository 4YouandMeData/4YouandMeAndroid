package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoFragmentDirections
import org.fouryouandme.auth.consent.informed.failure.ConsentInfoFailureFragmentDirections
import org.fouryouandme.auth.consent.informed.page.ConsentInfoPageFragmentDirections
import org.fouryouandme.auth.consent.informed.question.ConsentInfoQuestionFragmentDirections
import org.fouryouandme.auth.consent.informed.welcome.ConsentInfoWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentInfoWelcomeToConsentInfoPage(id: String): NavigationExecution =
    {
        it.navigate(
            ConsentInfoWelcomeFragmentDirections.actionConsentInfoWelcomeToConsentInfoPage(
                id
            )
        )
    }

fun consentInfoWelcomeToConsentInfoQuestion(index: Int): NavigationExecution =
    {
        it.navigate(
            ConsentInfoWelcomeFragmentDirections
                .actionConsentInfoWelcomeToConsentInfoQuestion(index)
        )
    }

fun consentInfoPageToConsentInfoPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentInfoPageFragmentDirections.actionConsentInfoPageSelf(id))
    }

fun consentInfoPageToConsentInfoModalPage(id: String): NavigationExecution =
    {
        it.navigate(ConsentInfoPageFragmentDirections.actionConsentInfoPageToConsentInfoModalPage(id))
    }

fun consentInfoPageToConsentInfoQuestion(index: Int): NavigationExecution =
    {
        it.navigate(
            ConsentInfoPageFragmentDirections.actionConsentInfoPageToConsentInfoQuestion(
                index
            )
        )
    }

fun consentInfoQuestionToConsentInfoQuestion(index: Int): NavigationExecution =
    {
        it.navigate(ConsentInfoQuestionFragmentDirections.actionConsentInfoQuestionSelf(index))
    }

fun consentInfoQuestionToConsentInfoSuccess(): NavigationExecution =
    {
        it.navigate(
            ConsentInfoQuestionFragmentDirections
                .actionConsentInfoQuestionToConsentInfoSuccess()
        )
    }

fun consentInfoQuestionToConsentInfoFailure(): NavigationExecution =
    {
        it.navigate(
            ConsentInfoQuestionFragmentDirections
                .actionConsentInfoQuestionToConsentInfoFailure()
        )
    }

fun consentInfoFailureToConsentInfoWelcome(): NavigationExecution =
    {
        it.popBackStack(R.id.consent_info_welcome, false)
    }

fun consentInfoFailureToConsentInfoPage(id: String): NavigationExecution =
    {
        it.navigate(
            ConsentInfoFailureFragmentDirections
                .actionConsentInfoFailureToConsentInfoPage(id)
        )
    }

fun consentInfoToConsentReview(): NavigationExecution =
    {
        it.navigate(ConsentInfoFragmentDirections.actionConsentInfoToConsentReview())
    }