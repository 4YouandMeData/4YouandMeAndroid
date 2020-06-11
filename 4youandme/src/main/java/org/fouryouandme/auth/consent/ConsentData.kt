package org.fouryouandme.auth.consent

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.entity.screening.Screening

data class ConsentState(
    val configuration: Option<Configuration> = None,
    val consent: Option<Consent> = None
)

sealed class ConsentStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val consent: Consent
    ) : ConsentStateUpdate()

}

sealed class ConsentLoading {

    object Initialization : ConsentLoading()

}

sealed class ConsentError {

    object Initialization : ConsentError()

}
