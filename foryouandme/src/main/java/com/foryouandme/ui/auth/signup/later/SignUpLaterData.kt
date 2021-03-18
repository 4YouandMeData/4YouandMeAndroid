package com.foryouandme.ui.auth.signup.later

import com.foryouandme.entity.configuration.Configuration

data class SignUpLaterState(val configuration: Configuration? = null)

sealed class SignUpLaterStateUpdate {

    data class Config(val configuration: Configuration) : SignUpLaterStateUpdate()

}

sealed class SignUpLaterLoading {

    object Configuration : SignUpLaterLoading()

}

sealed class SignUpLaterError {

    object Configuration : SignUpLaterError()

}

sealed class SignUpLaterStateEvent {

    object GetConfiguration : SignUpLaterStateEvent()
    object ScreenViewed : SignUpLaterStateEvent()

}