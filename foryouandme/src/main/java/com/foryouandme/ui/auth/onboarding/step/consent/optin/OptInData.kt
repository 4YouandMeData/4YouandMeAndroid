package com.foryouandme.ui.auth.onboarding.step.consent.optin

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.optins.OptIns

data class OptInState(
    val optIns: OptIns? = null,
    val permissions: Map<Int, Boolean> = emptyMap()
)

sealed class OptInStateUpdate {

    object OptIn : OptInStateUpdate()
    object Permissions : OptInStateUpdate()

}

sealed class OptInLoading {

    object OptIn : OptInLoading()
    object Permission : OptInLoading()

}

sealed class OptInError {

    object OptIn : OptInError()
    object Permission : OptInError()

}

sealed class OptInStateEvent {

    object GetOptIn : OptInStateEvent()

    data class RequestPermission(
        val configuration: Configuration,
        val index: Int
    ) : OptInStateEvent()

    data class SetPermission(
        val id: Int,
        val agree: Boolean
    ) : OptInStateEvent()

}

/* --- navigation --- */

data class OptInWelcomeToOptInPermission(val index: Int) : NavigationAction
data class OptInPermissionToOptInPermission(val index: Int) : NavigationAction
object OptInPermissionToOptInSuccess : NavigationAction
object OptInToConsentUser : NavigationAction