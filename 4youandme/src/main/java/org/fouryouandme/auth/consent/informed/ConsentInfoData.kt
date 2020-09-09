package org.fouryouandme.auth.consent.informed

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.auth.consent.informed.question.ConsentAnswerItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo

data class ConsentInfoState(
    val configuration: Option<Configuration> = None,
    val consentInfo: Option<ConsentInfo> = None,
    val questions: Map<String, List<ConsentAnswerItem>> = emptyMap()
)

sealed class ConsentInfoStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val questions: Map<String, List<ConsentAnswerItem>>,
        val consentInfo: ConsentInfo
    ) : ConsentInfoStateUpdate()

    data class Questions(
        val questions: Map<String, List<ConsentAnswerItem>>
    ) : ConsentInfoStateUpdate()

}

sealed class ConsentInfoLoading {

    object Initialization : ConsentInfoLoading()

}

sealed class ConsentInfoError {

    object Initialization : ConsentInfoError()

}

/* --- navigation --- */

data class ConsentInfoWelcomeToConsentInfoPage(val id: String) : NavigationAction
data class ConsentInfoWelcomeToConsentInfoQuestion(val index: Int) : NavigationAction
data class ConsentInfoPageToConsentInfoPage(val id: String) : NavigationAction
data class ConsentInfoPageToConsentInfoModalPage(val id: String) : NavigationAction
data class ConsentInfoPageToConsentInfoQuestion(val index: Int) : NavigationAction
data class ConsentInfoQuestionToConsentInfoQuestion(val index: Int) : NavigationAction
object ConsentInfoQuestionToConsentInfoSuccess : NavigationAction
object ConsentInfoQuestionToConsentInfoFailure : NavigationAction
data class ConsentInfoFailureToConsentInfoPage(val id: String) : NavigationAction
object ConsentInfoFailureToConsentInfoWelcome : NavigationAction
object ConsentInfoToConsentReview : NavigationAction
