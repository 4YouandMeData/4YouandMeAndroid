package org.fouryouandme.auth.consent.informed

import android.util.Log
import androidx.navigation.NavController
import arrow.Kind
import arrow.core.*
import arrow.core.extensions.fx
import arrow.fx.ForIO
import arrow.syntax.function.pipe
import org.fouryouandme.auth.consent.informed.question.ConsentAnswerItem
import org.fouryouandme.auth.consent.informed.question.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.AnywhereToWelcome
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.common.AnswerUseCase
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.informed.ConsentInfoUseCase
import org.fouryouandme.core.ext.countAndAccumulate
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync

class ConsentInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ConsentInfoState,
        ConsentInfoStateUpdate,
        ConsentInfoError,
        ConsentInfoLoading>
    (ConsentInfoState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentInfoLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ConsentInfoUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ConsentInfoError.Initialization) },
                { pair ->

                    val questions =
                        pair.first.questions.associateWith { question ->
                            question.answers.map { it.toItem(pair.second) }.all
                        }.mapKeys { it.key.id }

                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            consentInfo = pair.first.toOption(),
                            questions = questions
                        ),
                        ConsentInfoStateUpdate.Initialization(pair.second, questions, pair.first)
                    )
                }
            )

            !hideLoading(ConsentInfoLoading.Initialization)

        }.unsafeRunAsync()

    /* --- answer --- */

    fun getAnswers(index: Int): Option<List<ConsentAnswerItem>> =
        Option.fx {

            val questionId =
                !state().consentInfo.bind().questions.getOrNull(index)?.id.toOption()

            state().questions.getOption(questionId).bind()
        }

    fun answer(index: Int, answerId: String): Option<Unit> =

        Option.fx {

            val questionId =
                !state().consentInfo.bind().questions.getOrNull(index)?.id.toOption()

            val questions =
                state().questions.mapValues {
                    if (it.key == questionId)
                        it.value.map { item ->
                            item.copy(isSelected = item.answer.id == answerId)
                        }
                    else it.value
                }

            setState(
                state().copy(questions = questions),
                ConsentInfoStateUpdate.Questions(questions)
            ).unsafeRunAsync()
        }

    /* --- validation --- */

    private fun validate(
        navController: NavController,
        rootNavController: RootNavController
    ): Kind<ForIO, Unit> =
        runtime.fx.concurrent {

            val correctAnswers =
                state().questions
                    .mapValues { it.isCorrect() toT it.getRequest(rootNavController) }
                    .toList()
                    .map { it.second }
                    .countAndAccumulate()

            correctAnswers.b.parSequence().unsafeRunAsync()

            if (correctAnswers.a >= state().consentInfo.map { it.minimumAnswer }
                    .getOrElse { state().questions.size })
                !navigator.navigateTo(
                    runtime, navController,
                    ConsentInfoQuestionToConsentInfoSuccess
                )
            else
                !navigator.navigateTo(
                    runtime, navController,
                    ConsentInfoQuestionToConsentInfoFailure
                )

        }

    private fun Map.Entry<String, List<ConsentAnswerItem>>.getRequest(
        rootNavController: RootNavController
    ): Option<Kind<ForIO, Either<FourYouAndMeError, Unit>>> =

        value.firstOrNone { item -> item.isSelected }
            .map {
                AnswerUseCase.sendAnswer(
                    runtime,
                    key, // question id
                    it.answer.text,
                    it.answer.id
                ).handleAuthError(runtime, rootNavController, navigator)
            }

    private fun Map.Entry<String, List<ConsentAnswerItem>>.isCorrect() =
        value.fold(
            false,
            { acc, answerItem ->
                acc || (answerItem.answer.correct && answerItem.isSelected)
            }
        )

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun page(navController: NavController, id: String, fromWelcome: Boolean): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if (fromWelcome) ConsentInfoWelcomeToConsentInfoPage(
                id
            ) else ConsentInfoPageToConsentInfoPage(
                id
            )
        ).unsafeRunAsync()

    fun modalPage(navController: NavController, id: String): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ConsentInfoPageToConsentInfoModalPage(id)
        ).unsafeRunAsync()

    fun question(navController: NavController, fromWelcome: Boolean): Unit =
        when {
            fromWelcome -> ConsentInfoWelcomeToConsentInfoQuestion(
                0
            )
            else -> ConsentInfoPageToConsentInfoQuestion(
                0
            )
        }.pipe { navigator.navigateTo(runtime, navController, it) }.unsafeRunAsync()

    fun nextQuestion(
        navController: NavController,
        rootNavController: RootNavController,
        currentIndex: Int
    ): Unit =
        runtime.fx.concurrent {

            !if (currentIndex < (state().questions.keys.size - 1))
                navigator.navigateTo(
                    runtime,
                    navController,
                    ConsentInfoQuestionToConsentInfoQuestion(
                        currentIndex + 1
                    )
                )
            else validate(navController, rootNavController)

        }.unsafeRunAsync()

    fun consentReview(navController: RootNavController): Unit =
        navigator.navigateTo(runtime, navController, ConsentInfoToConsentReview).unsafeRunAsync()

    fun restartFromWelcome(navController: NavController): Unit =
        runtime.fx.concurrent {

            // reset old answer
            val questions =
                state().questions.mapValues { entry ->
                    entry.value.map { it.copy(isSelected = false) }
                }

            !setState(
                state().copy(questions = questions),
                ConsentInfoStateUpdate.Questions(questions)
            )

            !navigator.navigateTo(
                runtime, navController,
                ConsentInfoFailureToConsentInfoWelcome
            )

        }.unsafeRunAsync()

    fun restartFromPage(navController: NavController, id: String): Unit =
        runtime.fx.concurrent {

            // reset old answer
            val questions =
                state().questions.mapValues { entry ->
                    entry.value.map { it.copy(isSelected = false) }
                }

            !setState(
                state().copy(questions = questions),
                ConsentInfoStateUpdate.Questions(questions)
            )

            !navigator.navigateTo(
                runtime, navController,
                ConsentInfoFailureToConsentInfoPage(
                    id
                )
            )

        }.unsafeRunAsync()

    fun abort(navController: RootNavController): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWelcome).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()
}