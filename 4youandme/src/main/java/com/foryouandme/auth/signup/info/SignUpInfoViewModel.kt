package com.foryouandme.auth.signup.info

import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator

class SignUpInfoViewModel(
    navigator: Navigator,
) : BaseViewModel<
        Empty,
        Empty,
        Empty,
        Empty>
    (navigator, Empty) {

    /* --- navigation --- */

    suspend fun signUpLater(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SignUpInfoToSignUpLater)

    suspend fun enterPhone(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SignUpInfoToEnterPhone)

}