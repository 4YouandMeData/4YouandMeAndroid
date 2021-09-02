package com.foryouandme.ui.auth.signup.later

import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration

data class SignUpLaterState(
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class SignUpLaterAction {

    object GetConfiguration: SignUpLaterAction()
    object ScreenViewed : SignUpLaterAction()

}