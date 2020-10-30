package com.fouryouandme.studyinfo

import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.ConfigurationModule
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.fouryouandme.main.MainPageToAboutYouPage
import com.fouryouandme.main.MainPageToHtmlDetailsPage

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