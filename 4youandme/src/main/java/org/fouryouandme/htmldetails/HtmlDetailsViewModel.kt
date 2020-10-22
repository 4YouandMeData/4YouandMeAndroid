package org.fouryouandme.htmldetails

import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.StudyInfoModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.studyinfo.StudyInfoUseCase.getStudyInfo

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
    ): Either<FourYouAndMeError, HtmlDetailsState> {

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
        navigator.back(navController)
    }

}