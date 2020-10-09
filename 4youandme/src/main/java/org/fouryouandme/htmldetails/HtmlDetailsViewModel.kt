package org.fouryouandme.htmldetails

import androidx.navigation.NavController
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.StudyInfoModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.studyinfo.StudyInfoUseCase.getStudyInfo
import org.fouryouandme.core.ext.unsafeRunAsync

class HtmlDetailsViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val studyInfoModule: StudyInfoModule
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

    suspend fun initialize(
        rootNavController: RootNavController
    ): Either<FourYouAndMeError, HtmlDetailsState> {

        showLoadingFx(HtmlDetailsLoading.Initialization)

        val state =
            studyInfoModule.getStudyInfo()
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setErrorFx(it, HtmlDetailsError.Initialization)
                        it.left()
                    },
                    { studyInfo ->

                        val state = HtmlDetailsState(studyInfo)

                        setStateFx(state) { HtmlDetailsStateUpdate.Initialization(it.studyInfo) }

                        state.right()

                    }
                )

        hideLoadingFx(HtmlDetailsLoading.Initialization)

        return state
    }

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()
}