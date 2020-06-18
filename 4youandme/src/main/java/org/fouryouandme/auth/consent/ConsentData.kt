package org.fouryouandme.auth.consent

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.auth.consent.question.ConsentAnswerItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.Consent

data class ConsentState(
    val configuration: Option<Configuration> = None,
    val consent: Option<Consent> = None,
    val questions: Map<String, List<ConsentAnswerItem>> = emptyMap()
)

sealed class ConsentStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val questions: Map<String, List<ConsentAnswerItem>>,
        val consent: Consent
    ) : ConsentStateUpdate()

    data class Questions(
        val questions: Map<String, List<ConsentAnswerItem>>
    ) : ConsentStateUpdate()

}

sealed class ConsentLoading {

    object Initialization : ConsentLoading()

}

sealed class ConsentError {

    object Initialization : ConsentError()

}

/* --- navigation --- */

data class ConsentWelcomeToConsentPage(val id: String) : NavigationAction
data class ConsentWelcomeToConsentQuestion(val index: Int) : NavigationAction
data class ConsentPageToConsentPage(val id: String) : NavigationAction
data class ConsentPageToConsentQuestion(val index: Int) : NavigationAction
data class ConsentQuestionToConsentQuestion(val index: Int) : NavigationAction
object ConsentQuestionToConsentSuccess : NavigationAction
object ConsentQuestionToConsentFailure : NavigationAction
data class ConsentFailureToConsentPage(val id: String): NavigationAction
object ConsentFailureToConsentWelcome: NavigationAction
