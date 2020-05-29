package org.fouryouandme.auth.screening

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening

data class ScreeningState(
    val configuration: Option<Configuration> = None,
    val screening: Option<Screening> = None
)

sealed class ScreeningStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val screening: Screening
    ) : ScreeningStateUpdate()

}

sealed class ScreeningLoading {

    object Initialization : ScreeningLoading()

}

sealed class ScreeningError {

    object Initialization : ScreeningError()

}

/* --- navigator --- */

object ScreeningWelcomeToScreeningQuestions : NavigationAction