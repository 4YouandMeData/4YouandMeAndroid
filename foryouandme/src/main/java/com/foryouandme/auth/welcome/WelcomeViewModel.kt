package com.foryouandme.auth.welcome

import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.Navigator

class WelcomeViewModel(
    navigator: Navigator,
) : BaseViewModel<
        Empty,
        Empty,
        Empty,
        Empty>
    (navigator, Empty) {


    suspend fun signUpInfo(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, WelcomeToSignUpInfo)

}