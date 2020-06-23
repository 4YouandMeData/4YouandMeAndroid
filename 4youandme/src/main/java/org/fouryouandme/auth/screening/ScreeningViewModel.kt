package org.fouryouandme.auth.screening

import androidx.navigation.NavController
import arrow.core.None
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.auth.screening.questions.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.AnywhereToWelcome
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.screening.ScreeningUseCase
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync

class ScreeningViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ScreeningState,
        ScreeningStateUpdate,
        ScreeningError,
        ScreeningLoading>
    (ScreeningState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(navController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(ScreeningLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            val initialization =
                !configuration.foldToKindEither(runtime.fx) { config ->

                    ScreeningUseCase.getScreening(runtime)
                        .mapResult(runtime.fx) { it to config }

                }.handleAuthError(runtime, navController, navigator)

            !initialization.fold(
                { setError(it, ScreeningError.Initialization) },
                { pair ->
                    setState(
                        state().copy(
                            configuration = pair.second.toOption(),
                            screening = pair.first.toOption(),
                            questions = pair.first.questions.map { it.toItem(pair.second) }
                        ),
                        ScreeningStateUpdate.Initialization(pair.second, pair.first)
                    )
                }
            )

            !hideLoading(ScreeningLoading.Initialization)

        }.unsafeRunAsync()


    /* --- validation --- */

    fun validate(navController: NavController) {

        val validation =
            state().questions.map { item ->

                val answer =
                    when (item.answer.getOrElse { "" }) {
                        item.question.answers1.id -> item.question.answers1.some()
                        item.question.answers2.id -> item.question.answers2.some()
                        else -> None
                    }

                answer.map { it.correct }.getOrElse { false }

            }.fold(true, { acc, b -> acc && b })

        if (validation)
            navigator.navigateTo(runtime, navController, ScreeningQuestionsToScreeningSuccess)
                .unsafeRunAsync()
        else
            navigator.navigateTo(runtime, navController, ScreeningQuestionsToScreeningFailure)
                .unsafeRunAsync()

    }


    /* --- state update --- */

    fun answer(item: ScreeningQuestionItem): Unit {

        val questions =
            state().questions.map { if (it.question.id == item.question.id) item else it }

        setState(
            state().copy(questions = questions),
            ScreeningStateUpdate.Questions(questions)
        ).unsafeRunAsync()

    }


    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun questions(navController: NavController, fromWelcome: Boolean): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if (fromWelcome) ScreeningWelcomeToScreeningQuestions
            else ScreeningPageToScreeningQuestions
        ).unsafeRunAsync()

    fun page(navController: NavController, id: String, fromWelcome: Boolean): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            if (fromWelcome) ScreeningWelcomeToScreeningPage(id)
            else ScreeningPageToScreeningPage(id)
        ).unsafeRunAsync()

    fun consentInfo(navController: RootNavController): Unit =
        navigator.navigateTo(runtime, navController, ScreeningToConsentInfo).unsafeRunAsync()

    fun abort(navController: RootNavController): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWelcome).unsafeRunAsync()

    fun web(navController: RootNavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, AnywhereToWeb(url)).unsafeRunAsync()

    fun retryFromWelcome(navController: NavController): Unit =
        runtime.fx.concurrent {

            // reset old answers
            val questions =
                state().questions.map { it.copy(answer = None) }

            !setState(
                state().copy(questions = questions),
                ScreeningStateUpdate.Questions(questions)
            )

            !navigator.navigateTo(
                runtime,
                navController,
                ScreeningFailureToScreeningWelcome
            )

        }.unsafeRunAsync()
}