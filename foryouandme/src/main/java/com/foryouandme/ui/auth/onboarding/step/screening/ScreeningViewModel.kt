package com.foryouandme.ui.auth.onboarding.step.screening

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.countAndAccumulate
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.answer.SendAuthAnswerUseCase
import com.foryouandme.domain.usecase.auth.screening.GetScreeningUseCase
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.onboarding.step.screening.questions.ScreeningQuestionItem
import com.foryouandme.ui.auth.onboarding.step.screening.questions.toItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class ScreeningViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<ScreeningStateUpdate>,
    private val loadingFlow: LoadingFlow<ScreeningLoading>,
    private val errorFlow: ErrorFlow<ScreeningError>,
    private val navigationFlow: NavigationFlow,
    private val getScreeningUseCase: GetScreeningUseCase,
    private val sendAuthAnswerUseCase: SendAuthAnswerUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = ScreeningState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val error = errorFlow.error
    val loading = loadingFlow.loading
    val navigation = navigationFlow.navigation

    /* --- screening --- */

    private suspend fun getScreening(configuration: Configuration) {

        loadingFlow.show(ScreeningLoading.Screening)

        val screening = getScreeningUseCase()!!
        state = state.copy(
            screening = screening,
            questions = screening.questions.map { it.toItem(configuration) }
        )
        stateUpdateFlow.update(ScreeningStateUpdate.Screening)

        loadingFlow.hide(ScreeningLoading.Screening)

    }


    /* --- validation --- */

    private suspend fun validate() {
        coroutineScope {

            val correctAnswers =
                state.questions.map { item ->
                    val answer =
                        when (item.answer) {
                            item.question.answers1.id -> item.question.answers1
                            item.question.answers2.id -> item.question.answers2
                            else -> null
                        }

                    val request =
                        async {
                            sendAuthAnswerUseCase(
                                item.question.id,
                                answer?.text.orEmpty(),
                                answer?.id.orEmpty()
                            )
                        }

                    (answer?.correct ?: false) to request

                }.countAndAccumulate()

            viewModelScope.launchSafe { correctAnswers.second.awaitAll() }

            if (correctAnswers.first >= state.screening?.minimumAnswer ?: 0)
                navigationFlow.navigateTo(ScreeningQuestionsToScreeningSuccess)
            else
                navigationFlow.navigateTo(ScreeningQuestionsToScreeningFailure)

        }
    }


    /* --- answer --- */

    private suspend fun answer(item: ScreeningQuestionItem) {

        val questions =
            state.questions.map { if (it.question.id == item.question.id) item else it }

        state = state.copy(questions = questions)
        stateUpdateFlow.update(ScreeningStateUpdate.Questions)

    }

    /* --- retry --- */

    private suspend fun retryFromWelcome() {

        // reset old answers
        val questions = state.questions.map { it.copy(answer = null) }
        state = state.copy(questions = questions)
        stateUpdateFlow.update(ScreeningStateUpdate.Questions)

        navigationFlow.navigateTo(ScreeningFailureToScreeningWelcome)

    }

    /* --- analytics --- */

    private suspend fun logAbortEvent() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.CancelDuringScreeningQuestions,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: ScreeningStateEvent) {
        when (stateEvent) {
            is ScreeningStateEvent.GetScreening ->
                errorFlow.launchCatch(
                    viewModelScope,
                    ScreeningError.Screening,
                    loadingFlow,
                    ScreeningLoading.Screening
                ) { getScreening(stateEvent.configuration) }
            ScreeningStateEvent.Abort ->
                viewModelScope.launchSafe { logAbortEvent() }
            ScreeningStateEvent.Retry ->
                viewModelScope.launchSafe { retryFromWelcome() }
            is ScreeningStateEvent.Answer ->
                viewModelScope.launchSafe { answer(stateEvent.item) }
            ScreeningStateEvent.Validate ->
                viewModelScope.launchSafe { validate() }
        }
    }

}