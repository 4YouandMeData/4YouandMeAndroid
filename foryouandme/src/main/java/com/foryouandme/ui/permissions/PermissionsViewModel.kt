package com.foryouandme.ui.permissions

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
import com.foryouandme.ui.permissions.compose.PermissionsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(PermissionsState())
    val stateFlow = state as StateFlow<PermissionsState>

    private val eventChannel = Channel<PermissionsEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        execute(PermissionsAction.Initialize)
        execute(PermissionsAction.ScreenViewed)
    }

    /* --- initialize --- */

    private fun initialize(): Action =
        action(
            {
                coroutineScope {

                    state.emit(state.value.copy(data = state.value.data.toLoading()))

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
                            data = PermissionsData(
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
            eventChannel.send(PermissionsEvent.PermissionPermanentlyDenied)
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

    fun execute(action: PermissionsAction) {
        when (action) {
            PermissionsAction.Initialize ->
                viewModelScope.launchAction(initialize())
            is PermissionsAction.RequestPermissions ->
                viewModelScope.launchSafe { requestPermission(action.permission) }
            PermissionsAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}