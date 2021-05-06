package com.foryouandme.ui.aboutyou.permissions

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
import com.foryouandme.domain.usecase.permission.IsPermissionGrantedUseCase
import com.foryouandme.domain.usecase.permission.RequestPermissionUseCase
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AboutYouPermissionsViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(AboutYouPermissionsState())
    val stateFlow = state as StateFlow<AboutYouPermissionsState>

    private val eventChannel = Channel<AboutYouPermissionsEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        execute(AboutYouPermissionsAction.Initialize)
        execute(AboutYouPermissionsAction.ScreenViewed)
    }

    /* --- initialize --- */

    private fun initialize(): Action =
        action(
            {
                coroutineScope {

                    state.emit(state.value.copy(data = LazyData.Loading))

                    val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }
                    val isLocationGranted =
                        async { isPermissionGrantedUseCase(Permission.Location) }

                    val permissions =
                        listOf(
                            PermissionsItem(
                                configuration.await(),
                                "1",
                                Permission.Location,
                                configuration.await().text.profile.permissionLocation,
                                imageConfiguration.location(),
                                isLocationGranted.await()
                            )
                        )

                    state.emit(
                        state.value.copy(
                            data = AboutYouPermissionsData(
                                permissions = permissions,
                                configuration = configuration.await()
                            ).toData()
                        )
                    )

                }

            },
            { state.emit(state.value.copy(data = it.toError())) }
        )


    /* --- permissions ---- */

    private suspend fun requestPermission(permission: Permission) {

        val permissionRequest = requestPermissionUseCase(permission)

        if (
            permissionRequest is PermissionResult.Denied &&
            permissionRequest.isPermanentlyDenied
        )
            eventChannel.send(AboutYouPermissionsEvent.PermissionPermanentlyDenied)
        else {
            val data =
                state.value.data.map { data ->
                    data.copy(permissions = data.permissions.map {
                        if (it.permission == permission)
                            it.copy(isAllowed = permissionRequest is PermissionResult.Granted)
                        else
                            it
                    })
                }

            state.emit(state.value.copy(data = data))
        }

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.Permissions,
            EAnalyticsProvider.ALL
        )
    }

    /* --- actions --- */

    fun execute(action: AboutYouPermissionsAction) {
        when (action) {
            AboutYouPermissionsAction.Initialize ->
                viewModelScope.launchAction(initialize())
            is AboutYouPermissionsAction.RequestPermissions ->
                viewModelScope.launchSafe { requestPermission(action.permission) }
            AboutYouPermissionsAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}