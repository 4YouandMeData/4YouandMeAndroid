package com.foryouandme.ui.dailysurveytime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.domain.usecase.usersettings.GetUserSettingsUseCase
import com.foryouandme.domain.usecase.usersettings.UpdateUserSettingsUseCase
import com.foryouandme.entity.usersettings.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailySurveyTimeViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<DailySurveyTimeLoading>,
    private val errorFlow: ErrorFlow<DailySurveyTimeError>,
    private val stateUpdateFlow: StateUpdateFlow<DailySurveyTimeStateUpdate>,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase
) : ViewModel() {

    var state = DailySurveyTimeState()
        private set

    /* --- flow --- */

    val loading = loadingFlow.loading
    val error = errorFlow.error
    val stateUpdate = stateUpdateFlow.stateUpdates

    /* --- user settings --- */

    private suspend fun getUserSettings() {
        loadingFlow.show(DailySurveyTimeLoading.GetUserSettings)

        val userSettings = getUserSettingsUseCase()
        state = state.copy(userSettings = userSettings)

        stateUpdateFlow.update(DailySurveyTimeStateUpdate.GetUserSettings)
        loadingFlow.hide(DailySurveyTimeLoading.GetUserSettings)
    }

    private suspend fun saveUserSettings(userSettings: UserSettings) {
        loadingFlow.show(DailySurveyTimeLoading.SaveUserSettings)

       updateUserSettingsUseCase(userSettings)

        stateUpdateFlow.update(DailySurveyTimeStateUpdate.SaveUserSettings)
        loadingFlow.hide(DailySurveyTimeLoading.SaveUserSettings)
    }

    /* --- state event --- */

    fun execute(stateEvent: DailySurveyTimeStateEvent) {
        when (stateEvent) {
            DailySurveyTimeStateEvent.GetUserSettings ->
                errorFlow.launchCatch(
                    viewModelScope,
                    DailySurveyTimeError.GetUserSettings,
                    loadingFlow,
                    DailySurveyTimeLoading.GetUserSettings
                ) { getUserSettings() }
            is DailySurveyTimeStateEvent.SaveUserSettings ->
                errorFlow.launchCatch(
                    viewModelScope,
                    DailySurveyTimeError.SaveUserSettings,
                    loadingFlow,
                    DailySurveyTimeLoading.SaveUserSettings
                ) { saveUserSettings(UserSettings(stateEvent.dailySurveyTime)) }
        }
    }
}