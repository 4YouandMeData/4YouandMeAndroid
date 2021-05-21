package com.foryouandme.ui.aboutyou

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
import com.foryouandme.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AboutYouViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(AboutYouState())
    val stateFlow = state as StateFlow<AboutYouState>

    init {
        execute(AboutYouAction.ScreenViewed)
        execute(AboutYouAction.GetConfiguration)
        execute(AboutYouAction.GetUser)
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

    /* --- user --- */

    private fun getUser(): Action =
        action(
            {
                state.emit(state.value.copy(user = state.value.user.toLoading()))
                val user = getUserUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(user = user.toData()))
            },
            { state.emit(state.value.copy(user = it.toError())) }
        )

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.AboutYou,
            EAnalyticsProvider.ALL
        )
    }

    /* --- action --- */

    fun execute(action: AboutYouAction) {
        when (action) {
            AboutYouAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
            AboutYouAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            AboutYouAction.GetUser ->
                viewModelScope.launchAction(getUser())
        }
    }

}
