package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.R
import com.fouryouandme.auth.screening.ScreeningFragmentDirections
import com.fouryouandme.auth.screening.page.ScreeningPageFragmentDirections
import com.fouryouandme.auth.screening.questions.ScreeningQuestionsFragmentDirections
import com.fouryouandme.auth.screening.welcome.ScreeningWelcomeFragmentDirections
import com.fouryouandme.core.arch.navigation.NavigationExecution

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