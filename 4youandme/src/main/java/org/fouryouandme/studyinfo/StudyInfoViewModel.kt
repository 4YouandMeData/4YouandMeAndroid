package org.fouryouandme.studyinfo

import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.main.MainPageToAboutYouPage
import org.fouryouandme.main.MainPageToHtmlDetailsPage

class StudyInfoViewModel(
    navigator: Navigator,
    private val configurationModule: ConfigurationModule
) : BaseViewModel<
        StudyInfoState,
        StudyInfoStateUpdate,
        StudyInfoError,
        StudyInfoLoading>
    (
    navigator = navigator,
) {

    /* --- data --- */

    suspend fun initialize(): Unit {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)

        configuration.fold(
            { setError(it, StudyInfoError.Initialization) },
            {
                setState(
                    StudyInfoState(it),
                ) { state ->
                    StudyInfoStateUpdate.Initialization(state.configuration)
                }
            }
        )
    }

    /* --- navigation --- */

    suspend fun aboutYouPage(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToAboutYouPage
        )

    suspend fun detailsPage(navController: RootNavController, pageId: Int): Unit =
        navigator.navigateTo(
            navController,
            MainPageToHtmlDetailsPage(pageId)
        )
}