package org.fouryouandme.auth.screening

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening

data class ScreeningState(
    val configuration: Option<Configuration> = None,
    val screening: Option<Screening> = None,
    val questions: List<ScreeningQuestionItem> = emptyList()
)

sealed class ScreeningStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val screening: Screening
    ) : ScreeningStateUpdate()

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
object ScreeningQuestionsToScreeningSuccess : NavigationAction
object ScreeningQuestionsToScreeningFailure : NavigationAction
