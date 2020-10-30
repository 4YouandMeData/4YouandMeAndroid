package com.foryouandme.auth.consent.informed

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.toT
import arrow.fx.coroutines.parSequence
import arrow.syntax.function.pipe
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.consent.informed.question.ConsentAnswerItem
import com.foryouandme.auth.consent.informed.question.toItem
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnswerModule
import com.foryouandme.core.arch.deps.modules.ConsentInfoModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.AnywhereToWelcome
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.common.AnswerUseCase.sendAnswer
import com.foryouandme.core.cases.consent.informed.ConsentInfoUseCase.getConsent
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.countAndAccumulate
import com.foryouandme.core.ext.startCoroutineAsync

class ConsentInfoViewModel(
    navigator: Navigator,
    private val consentInfoModule: ConsentInfoModule,
    private val answerModule: AnswerModule
) : BaseViewModel<
        ConsentInfoState,
        ConsentInfoStateUpdate,
        ConsentInfoError,
        ConsentInfoLoading>
    (navigator = navigator) {

    /* --- data --- */

    suspend fun initialize(
        navController: RootNavController,
        configuration: Configuration
    ): Either<ForYouAndMeError, ConsentInfoState> {

        showLoading(ConsentInfoLoading.Initialization)

        val state =
            consentInfoModule.getConsent()
                .nullToError()
                .handleAuthError(navController, navigator)
                .fold(
                    {
                        setError(it, ConsentInfoError.Initialization)
                        it.left()
                    },
                    { consentInfo ->

                        val questions =
                            consentInfo.questions.associateWith { question ->
                                question.answers.map { it.toItem(configuration) }.all
                            }.mapKeys { it.key.id }

                        val state =
                            ConsentInfoState(consentInfo, questions)

                        setState(state)
                        { ConsentInfoStateUpdate.Initialization(it.questions, it.consentInfo) }

                        state.right()

                    }
                )

        hideLoading(ConsentInfoLoading.Initialization)

        return state

    }

    /* --- answer --- */

    fun getAnswers(index: Int): List<ConsentAnswerItem>? =
        state().consentInfo.questions.getOrNull(index)?.id
            ?.let { state().questions[it] }


    suspend fun answer(index: Int, answerId: String): Unit {

        val questionId =
            state().consentInfo.questions.getOrNull(index)?.id

        val questions =
            state().questions.mapValues {
                if (it.key == questionId)
                    it.value.map { item ->
                        item.copy(isSelected = item.answer.id == answerId)
                    }
                else it.value
            }

        setState(state().copy(questions = questions))
        { ConsentInfoStateUpdate.Questions(questions) }

    }

    /* --- validation --- */

    private suspend fun validate(
        consentInfoNavController: ConsentInfoNavController,
        rootNavController: RootNavController
    ): Unit {

        val correctAnswers =
            state().questions
                .mapValues { it.isCorrect() toT it.getRequest(rootNavController) }
                .toList()
                .map { it.second }
                .countAndAccumulate()

        startCoroutineAsync { correctAnswers.b.parSequence() }

        if (correctAnswers.a >= state().consentInfo.minimumAnswer)
            navigator.navigateTo(
                consentInfoNavController,
                ConsentInfoQuestionToConsentInfoSuccess
            )
        else
            navigator.navigateTo(
                consentInfoNavController,
                ConsentInfoQuestionToConsentInfoFailure
            )

    }

    private fun Map.Entry<String, List<ConsentAnswerItem>>.getRequest(
        rootNavController: RootNavController
    ): (suspend () -> Either<ForYouAndMeError, Unit>)? =

        value.firstOrNull { item -> item.isSelected }
            ?.let {
                suspend {
                    answerModule.sendAnswer(
                        key, // question id
                        it.answer.text,
                        it.answer.id
                    ).handleAuthError(rootNavController, navigator)
                }
            }

    private fun Map.Entry<String, List<ConsentAnswerItem>>.isCorrect() =
        value.fold(
            false,
            { acc, answerItem ->
                acc || (answerItem.answer.correct && answerItem.isSelected)
            }
        )

    /* --- navigation --- */

    suspend fun back(
        consentInfoNavController: ConsentInfoNavController,
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(consentInfoNavController).not())
            if (navigator.back(authNavController).not())
                navigator.back(rootNavController)
    }

    suspend fun page(
        consentInfoNavController: ConsentInfoNavController,
        id: String,
        fromWelcome: Boolean
    ): Unit =
        navigator.navigateTo(
            consentInfoNavController,
            if (fromWelcome) ConsentInfoWelcomeToConsentInfoPage(id)
            else ConsentInfoPageToConsentInfoPage(id)
        )

    suspend fun modalPage(consentInfoNavController: ConsentInfoNavController, id: String): Unit =
        navigator.navigateTo(
            consentInfoNavController,
            ConsentInfoPageToConsentInfoModalPage(id)
        )

    suspend fun question(
        consentInfoNavController: ConsentInfoNavController,
        fromWelcome: Boolean
    ): Unit =
        when {
            fromWelcome -> ConsentInfoWelcomeToConsentInfoQuestion(
                0
            )
            else -> ConsentInfoPageToConsentInfoQuestion(
                0
            )
        }.pipe { navigator.navigateTo(consentInfoNavController, it) }

    suspend fun nextQuestion(
        consentInfoNavController: ConsentInfoNavController,
        rootNavController: RootNavController,
        currentIndex: Int
    ): Unit {

        if (currentIndex < (state().questions.keys.size - 1))
            navigator.navigateTo(
                consentInfoNavController,
                ConsentInfoQuestionToConsentInfoQuestion(currentIndex + 1)
            )
        else validate(consentInfoNavController, rootNavController)

    }

    suspend fun consentReview(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, ConsentInfoToConsentReview)

    suspend fun restartFromWelcome(consentInfoNavController: ConsentInfoNavController): Unit {

        // reset old answer
        val questions =
            state().questions.mapValues { entry ->
                entry.value.map { it.copy(isSelected = false) }
            }

        setState(state().copy(questions = questions))
        { ConsentInfoStateUpdate.Questions(questions) }


        navigator.navigateTo(
            consentInfoNavController,
            ConsentInfoFailureToConsentInfoWelcome
        )

    }

    suspend fun restartFromPage(
        consentInfoNavController: ConsentInfoNavController,
        id: String
    ): Unit {

        // reset old answer
        val questions =
            state().questions.mapValues { entry ->
                entry.value.map { it.copy(isSelected = false) }
            }

        setState(state().copy(questions = questions))
        { ConsentInfoStateUpdate.Questions(questions) }


        navigator.navigateTo(
            consentInfoNavController,
            ConsentInfoFailureToConsentInfoPage(id)
        )

    }

    suspend fun abort(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, AnywhereToWelcome)

    suspend fun web(rootNavController: RootNavController, url: String): Unit =
        navigator.navigateTo(rootNavController, AnywhereToWeb(url))
}