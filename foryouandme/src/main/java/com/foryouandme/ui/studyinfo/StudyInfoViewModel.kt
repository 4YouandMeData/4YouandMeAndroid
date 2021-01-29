package com.foryouandme.ui.studyinfo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.*
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import kotlinx.coroutines.flow.SharedFlow

class StudyInfoViewModel @ViewModelInject constructor(
    private val stateUpdateFlow: StateUpdateFlow<StudyInfoStateUpdate>,
    private val loadingFlow: LoadingFlow<StudyInfoLoading>,
    private val errorFlow: ErrorFlow<StudyInfoError>,
    private val getConfigurationUseCase: GetConfigurationUseCase
) : ViewModel() {

    /* --- state --- */

    var state = StudyInfoState()
        private set

    /* --- flow --- */

    val stateUpdate: SharedFlow<StudyInfoStateUpdate> = stateUpdateFlow.stateUpdates
    val loading: SharedFlow<UILoading<StudyInfoLoading>> = loadingFlow.loading
    val error: SharedFlow<UIError<StudyInfoError>> = errorFlow.error

    /* --- initialization --- */

    suspend fun initialize() {

        loadingFlow.show(StudyInfoLoading.Initialization)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        state = state.copy(configuration = configuration)
        stateUpdateFlow.update(StudyInfoStateUpdate.Initialization(configuration))

        loadingFlow.hide(StudyInfoLoading.Initialization)

    }

    /* --- state events --- */

    fun execute(stateEvent: StudyInfoStateEvent) {

        when (stateEvent) {
            StudyInfoStateEvent.Initialization ->
                errorFlow.launchCatch(viewModelScope, StudyInfoError.Initialization)
                { initialize() }
        }

    }

}