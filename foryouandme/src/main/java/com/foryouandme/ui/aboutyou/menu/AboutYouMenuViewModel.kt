package com.foryouandme.ui.aboutyou.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.ui.aboutyou.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AboutYouMenuViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(AboutYouMenuState())
    val stateFlow = state as StateFlow<AboutYouMenuState>

    init {
        execute(AboutYouMenuAction.ScreenViewed)
        execute(AboutYouMenuAction.GetConfiguration)
        execute(AboutYouMenuAction.GetUser)
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

    /* --- user --- */

    private fun getUser() : Action =
        action(
            {
                state.emit(state.value.copy(user = LazyData.Loading))
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

    fun execute(action: AboutYouMenuAction) {
        when(action) {
            AboutYouMenuAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
            AboutYouMenuAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            AboutYouMenuAction.GetUser ->
                viewModelScope.launchAction(getUser())
        }
    }

}
