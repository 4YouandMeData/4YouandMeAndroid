package com.foryouandme.ui.aboutyou.dailysurveytime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.domain.usecase.usersettings.GetUserSettingsUseCase
import com.foryouandme.domain.usecase.usersettings.UpdateUserSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutYouDailySurveyTimeViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<AboutYouDailySurveyTimeLoading>,
    private val errorFlow: ErrorFlow<AboutYouDailySurveyTimeError>,
    private val stateUpdateFlow: StateUpdateFlow<AboutYouDailySurveyTimeStateUpdate>,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase
) : ViewModel() {

    var state = AboutYouDailySurveyTimeState()
        private set

    /* --- flow --- */

    val loading = loadingFlow.loading
    val error = errorFlow.error
    val stateUpdate = stateUpdateFlow.stateUpdates

    /* --- user settings --- */

    private suspend fun getUserSettings() {
        loadingFlow.show(AboutYouDailySurveyTimeLoading.GetUserSettings)

        val userSettings = getUserSettingsUseCase()
        state = state.copy(userSettings = userSettings)

        stateUpdateFlow.update(AboutYouDailySurveyTimeStateUpdate.GetUserSettings)
        loadingFlow.hide(AboutYouDailySurveyTimeLoading.GetUserSettings)
    }

    /* --- state event --- */

    fun execute(stateEvent: AboutYouDailySurveyTimeStateEvent) {
        when (stateEvent) {
            AboutYouDailySurveyTimeStateEvent.GetUserSettings ->
                errorFlow.launchCatch(
                    viewModelScope,
                    AboutYouDailySurveyTimeError.GetUserSettings,
                    loadingFlow,
                    AboutYouDailySurveyTimeLoading.GetUserSettings
                ) {
                    getUserSettings()
                }
        }
    }
}