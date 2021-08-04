package com.foryouandme.ui.permissions

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
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.permission.IsPermissionGrantedUseCase
import com.foryouandme.domain.usecase.permission.RequestPermissionUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import com.foryouandme.ui.permissions.compose.PermissionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val isPermissionGrantedUseCase: IsPermissionGrantedUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration,
    val settings: StudySettings
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(PermissionsState())
    val stateFlow = state as StateFlow<PermissionsState>

    private val events = MutableSharedFlow<UIEvent<PermissionsEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<PermissionsEvent>>

    init {
        execute(PermissionsAction.ScreenViewed)
    }

    /* --- initialize --- */

    private fun initialize(): Action =
        action(
            {
                coroutineScope {

                    state.emit(state.value.copy(data = state.value.data.toLoading()))

                    val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }
                    val user = async { getUserUseCase(Policy.LocalFirst) }
                    val isLocationGranted =
                        async { isPermissionGrantedUseCase(Permission.Location) }

                    val permissions =
                        user.await()
                            .permissions
                            .mapNotNull {
                                if (it is Permission.Location && settings.isLocationPermissionEnabled)
                                    PermissionItem(
                                        "1",
                                        Permission.Location,
                                        configuration.await().text.profile.permissions.location,
                                        imageConfiguration.location(),
                                        isLocationGranted.await()
                                    )
                                else null

                            }

                    state.emit(
                        state.value.copy(
                            data = PermissionsData(
                                user = user.await(),
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
            events.emit(PermissionsEvent.PermissionPermanentlyDenied.toUIEvent())
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