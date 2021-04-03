package com.foryouandme.ui.auth.onboarding.step.screening

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.screening.Screening
import com.foryouandme.ui.auth.onboarding.step.screening.questions.ScreeningQuestionItem

data class ScreeningState(
    val screening: Screening? = null,
    val questions: List<ScreeningQuestionItem> = emptyList()
)

sealed class ScreeningStateUpdate {

    object Screening : ScreeningStateUpdate()
    object Questions : ScreeningStateUpdate()

}

sealed class ScreeningLoading {

    object Screening : ScreeningLoading()

}

sealed class ScreeningError {

    object Screening : ScreeningError()

}

sealed class ScreeningStateEvent {

    data class GetScreening(val configuration: Configuration) : ScreeningStateEvent()
    data class Answer(val item: ScreeningQuestionItem): ScreeningStateEvent()
    object Validate: ScreeningStateEvent()
    object Abort : ScreeningStateEvent()
    object Retry: ScreeningStateEvent()

}

/* --- navigator --- */

object ScreeningWelcomeToScreeningQuestions : NavigationAction
data class ScreeningWelcomeToScreeningPage(val id: String) : NavigationAction
data class ScreeningPageToScreeningPage(val id: String) : NavigationAction
object ScreeningPageToScreeningQuestions : NavigationAction
object ScreeningQuestionsToScreeningSuccess : NavigationAction
object ScreeningQuestionsToScreeningFailure : NavigationAction
object ScreeningFailureToScreeningWelcome : NavigationAction
