package org.fouryouandme.auth.questions.screening

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class ScreeningQuestionsState(val configuration: Option<Configuration> = None)

sealed class ScreeningQuestionsStateUpdate {
    data class Initialization(val configuration: Configuration) : ScreeningQuestionsStateUpdate()
}

sealed class ScreeningQuestionsError {
    object Initialization : ScreeningQuestionsError()
}

sealed class ScreeningQuestionsLoading {
    object Initialization : ScreeningQuestionsLoading()
}