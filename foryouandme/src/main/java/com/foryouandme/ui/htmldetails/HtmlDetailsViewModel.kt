package com.foryouandme.ui.htmldetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.study.GetStudyInfoUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HtmlDetailsViewModel @ViewModelInject constructor(
    private val stateUpdateFlow: StateUpdateFlow<HtmlDetailsStateUpdate>,
    private val loadingFlow: LoadingFlow<HtmlDetailsLoading>,
    private val errorFlow: ErrorFlow<HtmlDetailsError>,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getStudyInfoUseCase: GetStudyInfoUseCase
) : ViewModel() {

    /* --- state --- */

    var state: HtmlDetailsState = HtmlDetailsState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- initialization --- */

    private suspend fun initialize() {
        coroutineScope {

            loadingFlow.show(HtmlDetailsLoading.Initialization)

            val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }
            val studyInfo = async { getStudyInfoUseCase()!! }

            state = state.copy(configuration = configuration.await(), studyInfo = studyInfo.await())
            stateUpdateFlow.update(
                HtmlDetailsStateUpdate.Initialization(
                    configuration.await(),
                    studyInfo.await()
                )
            )

            loadingFlow.hide(HtmlDetailsLoading.Initialization)

        }
    }

    /* --- state event --- */

    fun execute(stateEvent: HtmlDetailsStateEvent) {

        when (stateEvent) {
            HtmlDetailsStateEvent.Initialize ->
                errorFlow.launchCatch(viewModelScope, HtmlDetailsError.Initialization)
                { initialize() }
        }

    }

}