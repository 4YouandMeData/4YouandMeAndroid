package com.foryouandme.ui.web

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.ui.studyinfo.StudyInfoAction
import com.foryouandme.ui.studyinfo.StudyInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private var state = MutableStateFlow(WebState())
    val stateFlow = state as StateFlow<WebState>

    init {
        execute(WebAction.GetConfiguration)
        execute(WebAction.ScreenViewed)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = LazyData.Loading))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(AnalyticsEvent.ScreenViewed.Browser, EAnalyticsProvider.ALL)
    }

    /* --- action --- */

    fun execute(action: WebAction) {
        when(action) {
            WebAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            WebAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}