package com.foryouandme.ui.auth.signup.info

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class SignUpInfoState(
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class SignUpInfoAction {

    object GetConfiguration: SignUpInfoAction()
    object ScreenViewed : SignUpInfoAction()

}

/* --- navigation --- */

object SignUpInfoToSignUpLater : NavigationAction

object SignUpInfoToEnterPhone : NavigationAction

object SignUpInfoToPinCode : NavigationAction