package com.foryouandme.ui.auth.signup.info

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration


sealed class SignUpInfoStateEvent {

    object ScreenViewed : SignUpInfoStateEvent()

}

/* --- navigation --- */

object SignUpInfoToSignUpLater : NavigationAction

object SignUpInfoToEnterPhone : NavigationAction

object SignUpInfoToPinCode : NavigationAction