package com.foryouandme.ui.dailysurveytime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.usersettings.GetUserSettingsUseCase
import com.foryouandme.domain.usecase.usersettings.UpdateUserSettingsUseCase
import com.foryouandme.entity.usersettings.UserSettings
import com.foryouandme.ui.compose.error.toForYouAndMeException
import com.foryouandme.ui.userInfo.UserInfoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.LocalTime
import javax.inject.Inject

@HiltViewModel
class DailySurveyTimeViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(DailySurveyTimeState())
    val stateFlow = state as StateFlow<DailySurveyTimeState>

    /* --- event --- */

    private val events = MutableSharedFlow<UIEvent<DailySurveyTimeEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<DailySurveyTimeEvent>>

    /* --- init --- */

    init {
        execute(DailySurveyTimeAction.GetConfiguration)
        execute(DailySurveyTimeAction.GetUserSettings)
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

    /* --- user settings --- */

    private fun getUserSettings(): Action =
        action(
            {
                state.emit(state.value.copy(userSettings = LazyData.Loading()))
                val userSettings = getUserSettingsUseCase()!!
                state.emit(state.value.copy(userSettings = userSettings.toData()))
            },
            {
                val update =
                    state.value.copy(
                        userSettings = LazyData.Error(it.toForYouAndMeException())
                    )
                state.emit(update)
            }
        )

    private fun saveUserSettings(): Action =
        action(
            {
                val userSettings = state.value.userSettings.dataOrNull()
                if(userSettings != null) {
                    state.emit(state.value.copy(save = LazyData.Loading()))
                    updateUserSettingsUseCase(userSettings)
                    state.emit(state.value.copy(save = LazyData.unit()))
                }
                events.emit(DailySurveyTimeEvent.Saved.toUIEvent())
            },
            {
                val error = it.toForYouAndMeException()
                val update = state.value.copy(save = LazyData.Error(error))
                state.emit(update)
                events.emit(DailySurveyTimeEvent.SaveError(error).toUIEvent())
            }
        )

    /* --- time --- */

    private suspend fun updateTime(time: LocalTime) {
        val settings = state.value.userSettings.map { it.copy(dailySurveyTime = time) }
        val update = state.value.copy(userSettings = settings)
        state.emit(update)
    }

    /* --- action --- */

    fun execute(action: DailySurveyTimeAction) {
        when (action) {
            DailySurveyTimeAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            DailySurveyTimeAction.GetUserSettings ->
                viewModelScope.launchAction(getUserSettings())
            is DailySurveyTimeAction.SaveUserSettings ->
                viewModelScope.launchAction(saveUserSettings())
            is DailySurveyTimeAction.UpdateTime ->
                viewModelScope.launchSafe { updateTime(action.time) }
        }
    }
}