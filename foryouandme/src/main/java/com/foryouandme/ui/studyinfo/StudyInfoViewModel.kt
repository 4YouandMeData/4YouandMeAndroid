package com.foryouandme.ui.studyinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.study.GetStudyInfoUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.ui.compose.error.toForYouAndMeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class StudyInfoViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getStudyInfoUseCase: GetStudyInfoUseCase,
    val imageConfiguration: ImageConfiguration,
    private val mutex: Mutex
) : ViewModel() {

    /* --- state --- */

    private val mutableStateFlow = MutableStateFlow(StudyInfoState())
    val stateFlow = mutableStateFlow.asStateFlow()

    private suspend fun emit(update: (StudyInfoState) -> StudyInfoState) {
        mutex.withLock { mutableStateFlow.emit(update(mutableStateFlow.value)) }
    }

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
                mutableStateFlow.emit(mutableStateFlow.value.copy(configuration = mutableStateFlow.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                mutableStateFlow.emit(mutableStateFlow.value.copy(configuration = configuration.toData()))

            },
            { mutableStateFlow.emit(mutableStateFlow.value.copy(configuration = it.toError())) }
        )

    /* --- study info --- */

    private fun getStudyInfo(): Action =
        action(
            {
                mutableStateFlow.emit(mutableStateFlow.value.copy(studyInfo = mutableStateFlow.value.studyInfo.toLoading()))
                val studyInfo = getStudyInfoUseCase(Policy.Network)!!
                mutableStateFlow.emit(mutableStateFlow.value.copy(studyInfo = studyInfo.toData()))
            },
            {
                mutableStateFlow.emit(mutableStateFlow.value.copy(studyInfo = it.toError()))
                events.emit(StudyInfoEvent.StudyInfoError(it.toForYouAndMeException()).toUIEvent())
            }
        )

    /* --- user --- */

    private fun getUser(): Action =
        action(
            {
                emit { it.copy(user = it.user.toLoading()) }
                val user = getUserUseCase(Policy.LocalFirst)
                emit { it.copy(user = user.toData()) }
            },
            { throwable ->
                emit { it.copy(user = throwable.toError()) }
            }
        )

    /* --- action --- */

    fun execute(action: StudyInfoAction) {
        when (action) {
            StudyInfoAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            StudyInfoAction.GetStudyInfo ->
                viewModelScope.launchAction(getStudyInfo())
            StudyInfoAction.GetUser ->
                viewModelScope.launchAction(getUser())
        }
    }

}