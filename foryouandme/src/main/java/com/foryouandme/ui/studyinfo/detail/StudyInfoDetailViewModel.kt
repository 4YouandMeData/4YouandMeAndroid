package com.foryouandme.ui.studyinfo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.study.GetStudyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StudyInfoDetailViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getStudyInfoUseCase: GetStudyInfoUseCase,
    private val savedStateHandle: SavedStateHandle,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(StudyInfoDetailState(type = getStudyInfoType()))
    val stateFlow = state as StateFlow<StudyInfoDetailState>

    init {
        execute(StudyInfoDetailAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = LazyData.Loading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)

                state.emit(state.value.copy(configuration = configuration.toData()))
                execute(StudyInfoDetailAction.GetStudyInfo)

            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- study info --- */

    private fun getStudyInfo(): Action =
        action(
            {
                state.emit(state.value.copy(studyInfo = LazyData.Loading()))
                val studyInfo = getStudyInfoUseCase(Policy.LocalFirst)!!

                state.emit(state.value.copy(studyInfo = studyInfo.toData()))

            },
            { state.emit(state.value.copy(studyInfo = it.toError())) }
        )

    /* --- page type --- */

    private fun getStudyInfoType(): EStudyInfoType =
        savedStateHandle.get<EStudyInfoType>("pageId")!!

    /* --- action --- */

    fun execute(action: StudyInfoDetailAction) {

        when (action) {
            StudyInfoDetailAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            StudyInfoDetailAction.GetStudyInfo ->
                viewModelScope.launchAction(getStudyInfo())
        }

    }

}