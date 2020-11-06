package com.foryouandme.auth.screening

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.toT
import arrow.fx.coroutines.parSequence
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.screening.questions.ScreeningQuestionItem
import com.foryouandme.auth.screening.questions.toItem
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AnswerModule
import com.foryouandme.core.arch.deps.modules.ScreeningModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.AnywhereToWelcome
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.common.AnswerUseCase.sendAnswer
import com.foryouandme.core.cases.screening.ScreeningUseCase.getScreening
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.countAndAccumulate
import com.foryouandme.core.ext.startCoroutineAsync

class ScreeningViewModel(
    navigator: Navigator,
    private val screeningModule: ScreeningModule,
    private val answerModule: AnswerModule,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        ScreeningState,
        ScreeningStateUpdate,
        ScreeningError,
        ScreeningLoading>
    (navigator = navigator) {

    /* --- initialize --- */

    suspend fun initialize(
        navController: RootNavController,
        configuration: Configuration
    ): Either<ForYouAndMeError, ScreeningState> {

        showLoading(ScreeningLoading.Initialization)

        val state =
            screeningModule.getScreening()
                .nullToError()
                .handleAuthError(navController, navigator)
                .fold(
                    {
                        setError(it, ScreeningError.Initialization)
                        it.left()
                    },
                    { screening ->

                        val state =
                            ScreeningState(
                                screening,
                                screening.questions.map { it.toItem(configuration) }
                            )

                        setState(state)
                        { ScreeningStateUpdate.Initialization(it.screening) }

                        state.right()

                    }
                )

        hideLoading(ScreeningLoading.Initialization)

        return state

    }


    /* --- validation --- */

    suspend fun validate(
        rootNavController: RootNavController,
        screeningNavController: ScreeningNavController
    ): Unit {

        val correctAnswers =
            state().questions.map { item ->
                val answer =
                    when (item.answer) {
                        item.question.answers1.id -> item.question.answers1
                        item.question.answers2.id -> item.question.answers2
                        else -> null
                    }

                val request =
                    suspend {
                        answerModule.sendAnswer(
                            item.question.id,
                            answer?.text.orEmpty(),
                            answer?.id.orEmpty()
                        ).handleAuthError(rootNavController, navigator)
                    }

                (answer?.correct ?: false) toT request

            }.countAndAccumulate()

        //correctAnswers.b.parSequence()  // await execution
        startCoroutineAsync { correctAnswers.b.parSequence() }

        if (correctAnswers.a >= state().screening.minimumAnswer)
            navigator.navigateTo(
                screeningNavController,
                ScreeningQuestionsToScreeningSuccess
            )
        else
            navigator.navigateTo(
                screeningNavController,
                ScreeningQuestionsToScreeningFailure
            )

    }


    /* --- state update --- */

    suspend fun answer(item: ScreeningQuestionItem): Unit {

        val questions =
            state().questions.map { if (it.question.id == item.question.id) item else it }

        setState(state().copy(questions = questions))
        { ScreeningStateUpdate.Questions(questions) }

    }


    /* --- navigation --- */

    suspend fun back(
        screeningNavController: ScreeningNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {

        if (navigator.back(screeningNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)

    }


    suspend fun questions(
        screeningNavController: ScreeningNavController,
        fromWelcome: Boolean
    ): Unit =
        navigator.navigateTo(
            screeningNavController,
            if (fromWelcome) ScreeningWelcomeToScreeningQuestions
            else ScreeningPageToScreeningQuestions
        )

    suspend fun page(
        screeningNavController: ScreeningNavController,
        id: String,
        fromWelcome: Boolean
    ): Unit =
        navigator.navigateTo(
            screeningNavController,
            if (fromWelcome) ScreeningWelcomeToScreeningPage(id)
            else ScreeningPageToScreeningPage(id)
        )

    suspend fun consentInfo(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, ScreeningToConsentInfo)

    suspend fun abort(authNavController: AuthNavController): Unit {
        logAbortEvent()
        navigator.navigateTo(authNavController, AnywhereToWelcome)
    }

    suspend fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(navController, AnywhereToWeb(url))

    suspend fun retryFromWelcome(screeningNavController: ScreeningNavController): Unit {

        // reset old answers
        val questions =
            state().questions.map { it.copy(answer = null) }

        setState(state().copy(questions = questions))
        { ScreeningStateUpdate.Questions(questions) }

        navigator.navigateTo(
            screeningNavController,
            ScreeningFailureToScreeningWelcome
        )

    }

    /* --- analytics --- */

    private suspend fun logAbortEvent(): Unit =
        analyticsModule.logEvent(
            AnalyticsEvent.CancelDuringScreeningQuestions,
            EAnalyticsProvider.ALL
        )

}