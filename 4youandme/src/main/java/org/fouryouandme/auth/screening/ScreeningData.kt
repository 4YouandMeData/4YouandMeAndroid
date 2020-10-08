package org.fouryouandme.auth.screening

import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.screening.Screening

data class ScreeningState(
    val screening: Screening,
    val questions: List<ScreeningQuestionItem> = emptyList()
)

sealed class ScreeningStateUpdate {

    data class Initialization(val screening: Screening) : ScreeningStateUpdate()

    data class Questions(val questions: List<ScreeningQuestionItem>) : ScreeningStateUpdate()

}

sealed class ScreeningLoading {

    object Initialization : ScreeningLoading()

}

sealed class ScreeningError {

    object Initialization : ScreeningError()

}

/* --- navigator --- */

object ScreeningWelcomeToScreeningQuestions : NavigationAction
data class ScreeningWelcomeToScreeningPage(val id: String) : NavigationAction
data class ScreeningPageToScreeningPage(val id: String) : NavigationAction
object ScreeningPageToScreeningQuestions : NavigationAction
object ScreeningQuestionsToScreeningSuccess : NavigationAction
object ScreeningQuestionsToScreeningFailure : NavigationAction
object ScreeningFailureToScreeningWelcome : NavigationAction
object ScreeningToConsentInfo : NavigationAction
