package org.fouryouandme.auth.screening

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import kotlinx.coroutines.delay
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
                {
                    setState(
                        state().copy(
                            configuration = it.second.toOption(),
                            screening = it.first.toOption()
                        ),
                        ScreeningStateUpdate.Initialization(it.second, it.first)
                    )
                }
            )

            !hideLoading(ScreeningLoading.Initialization)

        }.unsafeRunAsync()

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    fun questions(navController: NavController): Unit =
        navigator.navigateTo(
            runtime,
            navController,
            ScreeningWelcomeToScreeningQuestions
        ).unsafeRunAsync()
}