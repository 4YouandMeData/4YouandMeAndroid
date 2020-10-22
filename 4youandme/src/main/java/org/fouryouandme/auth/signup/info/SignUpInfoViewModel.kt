package org.fouryouandme.auth.signup.info

import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.navigation.Navigator

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