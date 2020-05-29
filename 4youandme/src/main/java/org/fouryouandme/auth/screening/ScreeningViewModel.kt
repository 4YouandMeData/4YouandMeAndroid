package org.fouryouandme.auth.screening

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.auth.screening.questions.ScreeningQuestionItem
import org.fouryouandme.auth.screening.questions.toItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
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

    fun initialize(navController: NavController): Unit =
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

    fun questions(navController: NavController): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ScreeningWelcomeToScreeningQuestions
        ).unsafeRunAsync()
}