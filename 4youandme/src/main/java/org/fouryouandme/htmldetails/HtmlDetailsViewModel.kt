package org.fouryouandme.htmldetails

import androidx.navigation.NavController
import arrow.fx.ForIO
import org.fouryouandme.aboutyou.AboutYouError
import org.fouryouandme.aboutyou.AboutYouState
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.htmldetails.HtmlDetailsError
import org.fouryouandme.htmldetails.HtmlDetailsLoading
import org.fouryouandme.htmldetails.HtmlDetailsState
import org.fouryouandme.htmldetails.HtmlDetailsStateUpdate
import org.fouryouandme.main.MainPageToAboutYouPage
import org.fouryouandme.main.MainPageToHtmlDetailsPage

class HtmlDetailsViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<
        ForIO,
        HtmlDetailsState,
        HtmlDetailsStateUpdate,
        HtmlDetailsError,
        HtmlDetailsLoading>
    (
    navigator = navigator,
    runtime = runtime
) {
    /* --- data --- */

    suspend fun initialize(): Unit {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)

        configuration.fold(
            { setErrorFx(it, HtmlDetailsError.Initialization) },
            {
                setStateFx(
                    HtmlDetailsState(it),
                ) { state ->
                    HtmlDetailsStateUpdate.Initialization(state.configuration)
                }
            }
        )
    }

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()
}