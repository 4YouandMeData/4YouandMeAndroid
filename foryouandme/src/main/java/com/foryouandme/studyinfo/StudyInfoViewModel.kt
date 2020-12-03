package com.foryouandme.studyinfo

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.ConfigurationModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.foryouandme.main.*

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

    suspend fun info(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToInformation
        )

    suspend fun reward(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToReward
        )

    suspend fun faq(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToFaq
        )

}