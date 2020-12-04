package com.foryouandme.ui.auth.onboarding.step.consent.informed

import com.foryouandme.ui.auth.onboarding.step.consent.informed.question.ConsentAnswerItem
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.consent.informed.ConsentInfo

data class ConsentInfoState(
    val consentInfo: ConsentInfo,
    val questions: Map<String, List<ConsentAnswerItem>>
)

sealed class ConsentInfoStateUpdate {

    data class Initialization(
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

sealed class ConsentInfoAbort {

    data class FromPage(val pageId: String): ConsentInfoAbort()

    data class FromQuestion(val questionId: String): ConsentInfoAbort()

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
