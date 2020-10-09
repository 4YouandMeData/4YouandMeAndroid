package org.fouryouandme.auth.signup.info

import arrow.fx.ForIO
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class SignUpInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        Empty,
        Empty,
        Empty,
        Empty>
    (Empty, navigator, runtime) {

    /* --- navigation --- */

    suspend fun signUpLater(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SignUpInfoToSignUpLater)

    suspend fun enterPhone(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SignUpInfoToEnterPhone)

}