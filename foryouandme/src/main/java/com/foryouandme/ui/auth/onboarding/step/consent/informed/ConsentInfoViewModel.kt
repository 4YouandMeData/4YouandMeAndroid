package com.foryouandme.ui.auth.onboarding.step.consent.informed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.AnywhereToWelcome
import com.foryouandme.core.ext.countAndAccumulate
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.answer.SendAuthAnswerUseCase
import com.foryouandme.domain.usecase.auth.consent.GetConsentInfoUseCase
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import com.foryouandme.ui.auth.onboarding.step.consent.informed.question.ConsentAnswerItem
import com.foryouandme.ui.auth.onboarding.step.consent.informed.question.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class ConsentInfoViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ConsentInfoStateUpdate>,
    private val loadingFlow: LoadingFlow<ConsentInfoLoading>,
    private val errorFlow: ErrorFlow<ConsentInfoError>,
    private val navigationFlow: NavigationFlow,
    private val getConsentInfoUseCase: GetConsentInfoUseCase,
    private val sendAuthAnswerUseCase: SendAuthAnswerUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = ConsentInfoState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- consent info --- */

    private suspend fun getConsentInfo(configuration: Configuration) {

        loadingFlow.show(ConsentInfoLoading.ConsentInfo)

        val consentInfo = getConsentInfoUseCase()!!
        val questions =
            consentInfo.questions.associateWith { question ->
                question.answers.map { it.toItem(configuration) }
            }.mapKeys { it.key.id }

        state = state.copy(consentInfo = consentInfo, questions = questions)
        stateUpdateFlow.update(ConsentInfoStateUpdate.ConsentInfo)

        loadingFlow.hide(ConsentInfoLoading.ConsentInfo)

    }

    /* --- answer --- */

    fun getAnswers(index: Int): List<ConsentAnswerItem>? =
        state.consentInfo?.questions?.getOrNull(index)?.id?.let { state.questions[it] }


    private suspend fun answer(index: Int, answerId: String) {

        val questionId = state.consentInfo?.questions?.getOrNull(index)?.id

        val questions =
            state.questions.mapValues {
                if (it.key == questionId)
                    it.value.map { item ->
                        item.copy(isSelected = item.answer.id == answerId)
                    }
                else it.value
            }

        state = state.copy(questions = questions)
        stateUpdateFlow.update(ConsentInfoStateUpdate.Questions)

    }

    /* --- validation --- */

    private suspend fun validate() {

        val correctAnswers =
            state.questions
                .mapValues { it.isCorrect() to it.getRequestAsync() }
                .toList()
                .map { it.second }
                .countAndAccumulate()

        viewModelScope.launchSafe { correctAnswers.second.awaitAll() }

        if (correctAnswers.first >= state.consentInfo?.minimumAnswer ?: 0)
            navigationFlow.navigateTo(ConsentInfoQuestionToConsentInfoSuccess)
        else
            navigationFlow.navigateTo(ConsentInfoQuestionToConsentInfoFailure)

    }

    private suspend fun Map.Entry<String, List<ConsentAnswerItem>>.getRequestAsync(
    ): Deferred<Unit>? =
        coroutineScope {
            value.firstOrNull { item -> item.isSelected }
                ?.let {
                    async {
                        sendAuthAnswerUseCase(
                            key, // question id
                            it.answer.text,
                            it.answer.id
                        )
                    }
                }
        }

    private fun Map.Entry<String, List<ConsentAnswerItem>>.isCorrect() =
        value.fold(
            false,
            { acc, answerItem ->
                acc || (answerItem.answer.correct && answerItem.isSelected)
            }
        )

    /* --- reset --- */

    private suspend fun restartFromWelcome() {

        // reset old answer
        val questions =
            state.questions.mapValues { entry ->
                entry.value.map { it.copy(isSelected = false) }
            }

        state = state.copy(questions = questions)
        stateUpdateFlow.update(ConsentInfoStateUpdate.Questions)


        navigationFlow.navigateTo(ConsentInfoFailureToConsentInfoWelcome)

    }

    private suspend fun restartFromPage(id: String) {

        // reset old answer
        val questions =
            state.questions.mapValues { entry ->
                entry.value.map { it.copy(isSelected = false) }
            }

        state = state.copy(questions = questions)
        stateUpdateFlow.update(ConsentInfoStateUpdate.Questions)

        navigationFlow.navigateTo(ConsentInfoFailureToConsentInfoPage(id))

    }

    /* --- question --- */

    suspend fun nextQuestion(currentIndex: Int) {

        if (currentIndex < (state.questions.keys.size - 1))
            navigationFlow.navigateTo(
                ConsentInfoQuestionToConsentInfoQuestion(currentIndex + 1)
            )
        else validate()

    }

    /* --- abort --- */

    suspend fun abort(abort: ConsentInfoAbort) {

        when (abort) {
            is ConsentInfoAbort.FromPage -> logAbortFromPageEvent(abort.pageId)
            is ConsentInfoAbort.FromQuestion -> logAbortFromQuestionEvent(abort.questionId)
        }

    }

    /* --- analytics --- */

    private suspend fun logAbortFromPageEvent(pageId: String) {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.CancelDuringInformedConsent(pageId),
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logAbortFromQuestionEvent(questionId: String) {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.CancelDuringComprehension(questionId),
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: ConsentInfoStateEvent) {
        when (stateEvent) {
            is ConsentInfoStateEvent.GetConsentInfo ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ConsentInfoError.ConsentInfo,
                    loadingFlow,
                    ConsentInfoLoading.ConsentInfo
                ) { getConsentInfo(stateEvent.configuration) }
            is ConsentInfoStateEvent.Answer ->
                viewModelScope.launchSafe { answer(stateEvent.index, stateEvent.answerId) }
            is ConsentInfoStateEvent.RestartFromPage ->
                viewModelScope.launchSafe { restartFromPage(stateEvent.id) }
            ConsentInfoStateEvent.RestartFromWelcome ->
                viewModelScope.launchSafe { restartFromWelcome() }
            is ConsentInfoStateEvent.NextQuestion ->
                viewModelScope.launchSafe { nextQuestion(stateEvent.currentIndex) }
            is ConsentInfoStateEvent.Abort ->
                viewModelScope.launchSafe { abort(stateEvent.consentInfoAbort) }
        }
    }

}

