package com.foryouandme.ui.appsanddevices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.entity.integration.IntegrationApp.*
import com.foryouandme.ui.appsanddevices.compose.AppsAndDeviceItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppsAndDevicesViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    private val environment: Environment,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(AppsAndDevicesState())
    val stateFlow = state as StateFlow<AppsAndDevicesState>

    init {
        execute(AppsAndDevicesAction.GetConfiguration)
        execute(AppsAndDevicesAction.ScreenViewed)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
                execute(AppsAndDevicesAction.GetIntegrations)

            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /*--- integrations ---*/

    private fun getIntegrations(): Action =
        action(
            {
                val configuration = state.value.configuration.dataOrNull()

                if (configuration != null) {

                    state.emit(
                        state.value.copy(
                            appsAndDevices = state.value.appsAndDevices.toLoading()
                        )
                    )

                    val user = getUserUseCase(Policy.Network)

                    val integrations =
                        configuration.integrations
                            .map {
                                when (it) {
                                    Garmin -> createGarminItem(
                                        user.identities.contains(Garmin)
                                    )
                                    Fitbit -> createFitbitItem(
                                        user.identities.contains(Fitbit)
                                    )
                                    Oura -> createOuraItem(
                                        user.identities.contains(Oura)
                                    )
                                    Instagram -> createInstagramItem(
                                        user.identities.contains(Instagram)
                                    )
                                    RescueTime -> createRescueTimeItem(
                                        user.identities.contains(RescueTime)
                                    )
                                    Twitter -> createTwitterItem(
                                        user.identities.contains(Twitter)
                                    )
                                }
                            }

                    state.emit(state.value.copy(appsAndDevices = integrations.toData()))

                } else execute(AppsAndDevicesAction.GetConfiguration)

            },
            { state.emit(state.value.copy(appsAndDevices = it.toError())) }
        )

    private fun createGarminItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            name = "Garmin",
            image = imageConfiguration.garmin(),
            isConnected = isConnected,
            connectLink = "${environment.getOAuthBaseUrl()}/users/integration_oauth/garmin",
            disconnectLink = "${environment.getOAuthBaseUrl()}/users/deauthenticate/garmin"
        )

    private fun createFitbitItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            name = "Fitbit",
            image = imageConfiguration.smartwatch(),
            isConnected = isConnected,
            connectLink = "${environment.getOAuthBaseUrl()}/users/integration_oauth/fitbit",
            disconnectLink = "${environment.getOAuthBaseUrl()}/users/deauthenticate/fitbit"
        )

    private fun createOuraItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            name = "Oura",
            image = imageConfiguration.oura(),
            isConnected = isConnected,
            connectLink = "${environment.getOAuthBaseUrl()}/users/integration_oauth/oura",
            disconnectLink = "${environment.getOAuthBaseUrl()}/users/deauthenticate/oura"
        )

    private fun createInstagramItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            name = "Instagram",
            image = imageConfiguration.instagram(),
            isConnected = isConnected,
            connectLink = "${environment.getOAuthBaseUrl()}/users/integration_oauth/instagram",
            disconnectLink = "${environment.getOAuthBaseUrl()}/users/deauthenticate/instagram"
        )

    private fun createRescueTimeItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            name = "RescueTime",
            image = imageConfiguration.rescuetime(),
            isConnected = isConnected,
            connectLink = "${environment.getOAuthBaseUrl()}/users/integration_oauth/rescuetime",
            disconnectLink = "${environment.getOAuthBaseUrl()}/users/deauthenticate/rescuetime"
        )

    private fun createTwitterItem(isConnected: Boolean): AppsAndDeviceItem =
        AppsAndDeviceItem(
            "Twitter",
            imageConfiguration.twitter(),
            isConnected,
            "${environment.getOAuthBaseUrl()}/users/integration_oauth/twitter",
            "${environment.getOAuthBaseUrl()}/users/deauthenticate/twitter"
        )

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.AppsAndDevices,
            EAnalyticsProvider.ALL
        )
    }

    /* --- action --- */

    fun execute(action: AppsAndDevicesAction) {
        when (action) {
            AppsAndDevicesAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            AppsAndDevicesAction.GetIntegrations ->
                viewModelScope.launchAction(getIntegrations())
            AppsAndDevicesAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}