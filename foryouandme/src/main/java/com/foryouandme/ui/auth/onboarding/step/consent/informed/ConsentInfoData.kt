package com.foryouandme.ui.auth.onboarding.step.consent.informed

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.ui.auth.onboarding.step.consent.informed.question.ConsentAnswerItem

data class ConsentInfoState(
    val consentInfo: ConsentInfo? = null,
    val questions: Map<String, List<ConsentAnswerItem>> = emptyMap()
)

sealed class ConsentInfoStateUpdate {

    object ConsentInfo : ConsentInfoStateUpdate()

    object Questions : ConsentInfoStateUpdate()

}

sealed class ConsentInfoLoading {

    object ConsentInfo : ConsentInfoLoading()

}

sealed class ConsentInfoError {

    object ConsentInfo : ConsentInfoError()

}

sealed class ConsentInfoStateEvent {

    data class GetConsentInfo(val configuration: Configuration) : ConsentInfoStateEvent()
    data class Answer(val index: Int, val answerId: String) : ConsentInfoStateEvent()
    object RestartFromWelcome : ConsentInfoStateEvent()
    data class RestartFromPage(val id: String) : ConsentInfoStateEvent()
    data class NextQuestion(val currentIndex: Int) : ConsentInfoStateEvent()
    data class Abort(val consentInfoAbort: ConsentInfoAbort) : ConsentInfoStateEvent()
    object LearnMoreViewed : ConsentInfoStateEvent()

}

sealed class ConsentInfoAbort {

    data class FromPage(val pageId: String) : ConsentInfoAbort()

    data class FromQuestion(val questionId: String) : ConsentInfoAbort()

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
