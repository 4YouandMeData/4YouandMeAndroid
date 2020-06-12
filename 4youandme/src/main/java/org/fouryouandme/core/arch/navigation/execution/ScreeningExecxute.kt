package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun screeningWelcomeToScreeningQuestions(): NavigationExecution = {

    it.navigate(R.id.action_screening_welcome_to_screening_questions)
}

fun screeningQuestionsToScreeningSuccess(): NavigationExecution = {

    it.navigate(R.id.action_screening_questions_to_screening_success)

}

fun screeningQuestionsToScreeningFailure(): NavigationExecution = {

    it.navigate(R.id.action_screening_questions_to_screening_failure)

}

fun screeningToConsent(): NavigationExecution = {

    it.navigate(R.id.action_screening_to_consent)

}