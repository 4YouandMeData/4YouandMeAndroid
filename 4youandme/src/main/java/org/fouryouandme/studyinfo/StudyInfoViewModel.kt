package org.fouryouandme.studyinfo

import arrow.fx.ForIO
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.main.MainStateUpdate
import org.fouryouandme.web.WebError

class StudyInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<
        ForIO,
        StudyInfoState,
        StudyInfoStateUpdate,
        StudyInfoError,
        StudyInfoLoading>
    (
    navigator = navigator,
    runtime = runtime
) {

    /* --- data --- */

    suspend fun initialize(): Unit {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)

        configuration.fold(
            { setErrorFx(it, StudyInfoError.Initialization) },
            {
                setStateFx(
                    StudyInfoState(it),
                ) { state ->
                    StudyInfoStateUpdate.Initialization(state.configuration)
                }
            }
        )
    }

}