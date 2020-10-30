package com.fouryouandme.auth.signup.info

import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.navigation.Navigator

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