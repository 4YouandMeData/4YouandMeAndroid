package com.foryouandme.ui.htmldetails

import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.StudyInfoModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.studyinfo.StudyInfoUseCase.getStudyInfo

class HtmlDetailsViewModel(
    navigator: Navigator,
    private val studyInfoModule: StudyInfoModule
) : BaseViewModel<
        HtmlDetailsState,
        HtmlDetailsStateUpdate,
        HtmlDetailsError,
        HtmlDetailsLoading>
    (
    navigator = navigator,
) {
    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<ForYouAndMeError, HtmlDetailsState> {

        showLoading(HtmlDetailsLoading.Initialization)

        val state =
            studyInfoModule.getStudyInfo()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, HtmlDetailsError.Initialization)
                        it.left()
                    },
                    { studyInfo ->

                        val state = HtmlDetailsState(studyInfo)

                        setState(state) { HtmlDetailsStateUpdate.Initialization(it.studyInfo) }

                        state.right()

                    }
                )

        hideLoading(HtmlDetailsLoading.Initialization)

        return state
    }

    /* --- navigation --- */

    suspend fun back(navController: NavController): Unit {
        navigator.backSuspend(navController)
    }

}