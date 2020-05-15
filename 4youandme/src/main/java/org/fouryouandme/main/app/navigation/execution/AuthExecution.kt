package org.fouryouandme.main.app.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.auth.phone.EnterPhoneFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun splashToWelcome(): NavigationExecution = {
    it.navigate(R.id.action_splash_to_welcome)
}

fun welcomeToSignUpInfo(): NavigationExecution = {
    it.navigate(R.id.action_welcome_to_sign_up_info)
}

fun signUpInfoToSignUpLater(): NavigationExecution = {
    it.navigate(R.id.action_sign_up_info_to_sign_up_later)
}

fun signUpInfoToEnterPhone(): NavigationExecution = {
    it.navigate(R.id.action_sign_up_info_to_enter_phone)
}

fun enterPhoneToPhoneValidationCode(): NavigationExecution = {
    it.navigate(R.id.action_enter_phone_to_phone_validation_code)
}

fun enterPhoneToWeb(url: String): NavigationExecution = {
    it.navigate(EnterPhoneFragmentDirections.actionSignUpInfoToWeb(url))
}