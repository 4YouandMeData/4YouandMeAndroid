package org.fouryouandme.auth.consent.informed

import androidx.navigation.NavController
import arrow.Kind
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOption
import arrow.core.toOption
import arrow.fx.ForIO
import arrow.syntax.function.pipe
import org.fouryouandme.auth.consent.informed.question.ConsentAnswerItem
import org.fouryouandme.auth.consent.informed.question.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.AnywhereToWelcome
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.consent.informed.ConsentInfoUseCase
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

    fun question(navController: NavController, fromWelcome: Boolean): Unit =
        when {
            fromWelcome -> ConsentInfoWelcomeToConsentInfoQuestion(
                0
            )
            else -> ConsentInfoPageToConsentInfoQuestion(
                0
            )
        }.pipe { navigator.navigateTo(runtime, navController, it) }.unsafeRunAsync()

    fun nextQuestion(navController: NavController, currentIndex: Int): Unit =
        runtime.fx.concurrent {

            !if (currentIndex < (state().questions.keys.size - 1))
                navigator.navigateTo(
                    runtime,
                    navController,
                    ConsentInfoQuestionToConsentInfoQuestion(
                        currentIndex + 1
                    )
                )
            else validate(navController)

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