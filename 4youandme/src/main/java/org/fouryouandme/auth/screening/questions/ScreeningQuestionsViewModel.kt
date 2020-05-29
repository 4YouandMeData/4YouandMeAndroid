package org.fouryouandme.auth.screening.questions

import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class ScreeningQuestionsViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        ScreeningQuestionsState,
        ScreeningQuestionsStateUpdate,
        ScreeningQuestionsError,
        ScreeningQuestionsLoading>
    (ScreeningQuestionsState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(): Unit =
        runtime.fx.concurrent {

            !showLoading(ScreeningQuestionsLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, ScreeningQuestionsError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        ScreeningQuestionsStateUpdate.Initialization(it)
                    )
                }
            )

            !hideLoading(ScreeningQuestionsLoading.Initialization)

        }.unsafeRunAsync()

}