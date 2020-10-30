package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.auth.consent.informed.ConsentInfoFragmentDirections
import com.foryouandme.auth.consent.informed.failure.ConsentInfoFailureFragmentDirections
import com.foryouandme.auth.consent.informed.page.ConsentInfoPageFragmentDirections
import com.foryouandme.auth.consent.informed.question.ConsentInfoQuestionFragmentDirections
import com.foryouandme.auth.consent.informed.welcome.ConsentInfoWelcomeFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

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