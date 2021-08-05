package com.foryouandme.ui.studyinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.*
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.study.GetStudyInfoUseCase
import com.foryouandme.ui.compose.error.toForYouAndMeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StudyInfoViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getStudyInfoUseCase: GetStudyInfoUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(StudyInfoState())
    val stateFlow = state as StateFlow<StudyInfoState>

    /* --- events --- */

    private val events = MutableSharedFlow<UIEvent<StudyInfoEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<StudyInfoEvent>>

    init {
        execute(StudyInfoAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))

            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- study info --- */

    private fun getStudyInfo(): Action =
        action(
            {
                state.emit(state.value.copy(studyInfo = state.value.studyInfo.toLoading()))
                val studyInfo = getStudyInfoUseCase(Policy.Network)!!
                state.emit(state.value.copy(studyInfo = studyInfo.toData()))
            },
            {
                state.emit(state.value.copy(studyInfo = it.toError()))
                events.emit(StudyInfoEvent.StudyInfoError(it.toForYouAndMeException()).toUIEvent())
            }
        )

    /* --- action --- */

    fun execute(action: StudyInfoAction) {
        when(action) {
            StudyInfoAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            StudyInfoAction.GetStudyInfo ->
                viewModelScope.launchAction(getStudyInfo())
        }
    }

}