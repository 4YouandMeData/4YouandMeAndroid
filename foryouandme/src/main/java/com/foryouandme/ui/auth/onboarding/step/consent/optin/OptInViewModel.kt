package com.foryouandme.ui.auth.onboarding.step.consent.optin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.action.alertAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.auth.consent.GetOptInsUseCase
import com.foryouandme.domain.usecase.auth.consent.SetOptInPermissionUseCase
import com.foryouandme.domain.usecase.permission.RequestPermissionUseCase
import com.foryouandme.domain.usecase.permission.RequestPermissionsUseCase
import com.foryouandme.entity.configuration.Configuration
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OptInViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<OptInStateUpdate>,
    private val loadingFlow: LoadingFlow<OptInLoading>,
    private val errorFlow: ErrorFlow<OptInError>,
    private val navigationFlow: NavigationFlow,
    private val navigator: Navigator,
    private val getOptInsUseCase: GetOptInsUseCase,
    private val setOptInPermissionUseCase: SetOptInPermissionUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val requestPermissionsUseCase: RequestPermissionsUseCase
) : ViewModel() {

    /* --- state --- */

    var state = OptInState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- opt ins --- */

    private suspend fun getOptIn() {

        loadingFlow.show(OptInLoading.OptIn)

        val optIns = getOptInsUseCase()!!

        state = state.copy(optIns = optIns, permissions = emptyMap())
        stateUpdateFlow.update(OptInStateUpdate.OptIn)

        loadingFlow.hide(OptInLoading.OptIn)

    }

    /* --- permission --- */

    private suspend fun setPermission(
        index: Int,
        permissionId: String,
        agree: Boolean
    ) {

        loadingFlow.show(OptInLoading.Permission)

        setOptInPermissionUseCase(permissionId, agree)
        nextPermission(index)

        loadingFlow.hide(OptInLoading.Permission)

    }

    private suspend fun nextPermission(currentIndex: Int) {

        if (state.optIns?.permissions?.size ?: 0 > (currentIndex + 1))
            navigationFlow.navigateTo(
                OptInPermissionToOptInPermission(currentIndex + 1)
            )
        else navigationFlow.navigateTo(OptInPermissionToOptInSuccess)

    }

    private suspend fun requestPermissions(configuration: Configuration, index: Int) {

        val agree = state.permissions[index]
        val optIn = state.optIns?.permissions?.getOrNull(index)

        if (agree != null && optIn != null) {
            if (agree) {

                when (optIn.systemPermissions.size) {
                    0 ->
                        setPermission(
                            index,
                            optIn.id,
                            agree
                        )
                    1 -> {
                        requestPermissionUseCase(optIn.systemPermissions[0])
                        setPermission(
                            index,
                            optIn.id,
                            agree
                        )
                    }
                    else -> {
                        requestPermissionsUseCase(optIn.systemPermissions)
                        setPermission(
                            index,
                            optIn.id,
                            agree
                        )
                    }
                }

            } else {

                if (optIn.mandatory)
                    alert(
                        configuration.text.onboarding.optIn.mandatoryTitle,
                        optIn.mandatoryDescription
                            ?: configuration.text.onboarding.optIn.mandatoryDefault,
                        configuration.text.onboarding.optIn.mandatoryClose
                    )
                else nextPermission(index)

            }
        }
    }

    private fun alert(title: String, message: String, close: String) {
        navigator.performAction(alertAction(title, message, close))
    }

    private suspend fun setPermissionState(id: Int, agree: Boolean) {

        val map = state.permissions.toMutableMap().also { it[id] = agree }

        state = state.copy(permissions = map)
        stateUpdateFlow.update(OptInStateUpdate.Permissions)

    }

    /* --- state event --- */

    fun execute(stateEvent: OptInStateEvent) {
        when (stateEvent) {
            OptInStateEvent.GetOptIn ->
                errorFlow.launchCatch(
                    viewModelScope,
                    OptInError.OptIn,
                    loadingFlow,
                    OptInLoading.OptIn
                ) { getOptIn() }
            is OptInStateEvent.SetPermission ->
                viewModelScope.launchSafe { setPermissionState(stateEvent.id, stateEvent.agree) }
            is OptInStateEvent.RequestPermission ->
                errorFlow.launchCatch(
                    viewModelScope,
                    OptInError.Permission,
                    loadingFlow,
                    OptInLoading.Permission
                ) { requestPermissions(stateEvent.configuration, stateEvent.index) }
        }
    }

}