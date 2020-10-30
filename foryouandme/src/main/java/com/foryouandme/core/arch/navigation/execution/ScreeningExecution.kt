package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.auth.screening.ScreeningFragmentDirections
import com.foryouandme.auth.screening.page.ScreeningPageFragmentDirections
import com.foryouandme.auth.screening.questions.ScreeningQuestionsFragmentDirections
import com.foryouandme.auth.screening.welcome.ScreeningWelcomeFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

fun screeningWelcomeToScreeningQuestions(): NavigationExecution = {
    it.navigate(ScreeningWelcomeFragmentDirections.actionScreeningWelcomeToScreeningQuestions())
}

fun screeningWelcomeToScreeningPage(id: String): NavigationExecution = {
    it.navigate(ScreeningWelcomeFragmentDirections.actionScreeningWelcomeToScreeningPage(id))
}

fun screeningPageToScreeningPage(id: String): NavigationExecution = {
    it.navigate(ScreeningPageFragmentDirections.actionScreeningPageSelf(id))
}

fun screeningPageToScreeningQuestions(): NavigationExecution = {
    it.navigate(ScreeningPageFragmentDirections.actionScreeningPageToScreeningQuestion())
}

fun screeningQuestionsToScreeningSuccess(): NavigationExecution = {
    it.navigate(ScreeningQuestionsFragmentDirections.actionScreeningQuestionsToScreeningSuccess())
}

fun screeningQuestionsToScreeningFailure(): NavigationExecution = {
    it.navigate(ScreeningQuestionsFragmentDirections.actionScreeningQuestionsToScreeningFailure())
}

fun screeningFailureToScreeningWelcome(): NavigationExecution = {
    it.popBackStack(R.id.screening_welcome, false)
}

fun screeningToConsentInfo(): NavigationExecution = {
    it.navigate(ScreeningFragmentDirections.actionScreeningToConsentInfo())
}