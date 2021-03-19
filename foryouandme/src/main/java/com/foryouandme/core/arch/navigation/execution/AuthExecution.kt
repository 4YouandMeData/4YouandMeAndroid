package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.ui.auth.signin.phone.EnterPhoneFragmentDirections
import com.foryouandme.ui.auth.signin.phone.code.PhoneValidationCodeFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.auth.splash.SplashFragmentDirections

fun splashToWelcome(): NavigationExecution = {
    it.navigate(R.id.action_splash_to_welcome)
}

fun splashToOnboarding(): NavigationExecution = {
    it.navigate(SplashFragmentDirections.actionSplashToOnboarding())
}

fun splashToMain(): NavigationExecution = {
    it.navigate(R.id.action_auth_to_main)
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

fun enterPhoneToPhoneValidationCode(phone: String, countryCode: String): NavigationExecution = {
    it.navigate(
        EnterPhoneFragmentDirections.actionEnterPhoneToPhoneValidationCode(phone, countryCode)
    )
}

fun phoneValidationCodeToOnboarding(): NavigationExecution = {
    it.navigate(PhoneValidationCodeFragmentDirections.actionPhoneValidationCodeToOnboarding())
}

fun phoneValidationCodeToMain(): NavigationExecution = {
    it.navigate(R.id.action_auth_to_main)
}

