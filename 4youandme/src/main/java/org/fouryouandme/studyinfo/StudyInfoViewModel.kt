package org.fouryouandme.studyinfo

import arrow.fx.ForIO
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.main.MainStateUpdate
import org.fouryouandme.web.WebError

class StudyInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        StudyInfoState,
        StudyInfoStateUpdate,
        StudyInfoError,
        StudyInfoLoading>
    (StudyInfoState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, StudyInfoError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it),
                        StudyInfoStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()
}