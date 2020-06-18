package org.fouryouandme.auth.consent

import androidx.navigation.NavController
import arrow.Kind
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOption
import arrow.core.toOption
import arrow.fx.ForIO
import arrow.syntax.function.pipe
import org.fouryouandme.auth.consent.question.ConsentAnswerItem
import org.fouryouandme.auth.consent.question.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.AnywhereToWelcome
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.ConsentUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync

class ConsentViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ConsentState,
        ConsentStateUpdate,
        ConsentError,
        ConsentLoading>
    (ConsentState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: NavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ConsentLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ConsentUseCase.getConsent(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ConsentError.Initialization) },
                { pair ->

                    val questions =
                        pair.first.questions.associateWith { question ->
                            question.answers.map { it.toItem(pair.second) }.all
                        }.mapKeys { it.key.id }

                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            consent = pair.first.toOption(),
                            questions = questions
                        ),
                        ConsentStateUpdate.Initialization(pair.second, questions, pair.first)
                    )
                }
            )

            !hideLoading(ConsentLoading.Initialization)

        }.unsafeRunAsync()

    /* --- answer --- */

    fun getAnswers(index: Int): Option<List<ConsentAnswerItem>> =
        Option.fx {

            val questionId =
                !state().consent.bind().questions.getOrNull(index)?.id.toOption()

            state().questions.getOption(questionId).bind()
        }

    fun answer(index: Int, answerId: String): Option<Unit> =

        Option.fx {

            val questionId =
                !state().consent.bind().questions.getOrNull(index)?.id.toOption()

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
                ConsentStateUpdate.Questions(questions)
            ).unsafeRunAsync()
        }

    /* --- validation --- */

    private fun validate(navController: NavController): Kind<ForIO, Unit> =
        runtime.fx.concurrent {

            val correctAnswers =
                state().questions
                    .mapValues {
                        it.value.fold(
                            false,
                            { acc, answerItem ->
                                acc || (answerItem.answer.correct && answerItem.isSelected)
                            }
                        )
                    }
                    .toList()
                    .fold(
                        0,
                        { acc, pair ->
                            acc + if (pair.second) 1 else 0
                        }
                    )

            if (correctAnswers >= 4)
                !navigator.navigateTo(runtime, navController, ConsentQuestionToConsentSuccess)
            else
                !navigator.navigateTo(runtime, navController, ConsentQuestionToConsentFailure)

        }

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun page(navController: NavController, id: String, fromWelcome: Boolean): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if (fromWelcome) ConsentWelcomeToConsentPage(id) else ConsentPageToConsentPage(id)
        ).unsafeRunAsync()

    fun question(navController: NavController, fromWelcome: Boolean): Unit =
        when {
            fromWelcome -> ConsentWelcomeToConsentQuestion(0)
            else -> ConsentPageToConsentQuestion(0)
        }.pipe { navigator.navigateTo(runtime, navController, it) }.unsafeRunAsync()

    fun nextQuestion(navController: NavController, currentIndex: Int): Unit =
        runtime.fx.concurrent {

            !if (currentIndex < (state().questions.keys.size - 1))
                navigator.navigateTo(
                    runtime,
                    navController,
                    ConsentQuestionToConsentQuestion(currentIndex + 1)
                )
            else validate(navController)

        }.unsafeRunAsync()

    fun restartFromWelcome(navController: NavController): Unit =
        runtime.fx.concurrent {

            // reset old answer
            val questions =
                state().questions.mapValues { entry ->
                    entry.value.map { it.copy(isSelected = false) }
                }

            !setState(
                state().copy(questions = questions),
                ConsentStateUpdate.Questions(questions)
            )

            !navigator.navigateTo(runtime, navController, ConsentFailureToConsentWelcome)

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
                ConsentStateUpdate.Questions(questions)
            )

            !navigator.navigateTo(runtime, navController, ConsentFailureToConsentPage(id))

        }.unsafeRunAsync()

    fun abort(navController: NavController): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWelcome).unsafeRunAsync()

    fun web(navController: NavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()
}