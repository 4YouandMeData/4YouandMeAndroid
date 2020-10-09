package org.fouryouandme.auth.welcome

import arrow.fx.ForIO
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class WelcomeViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        Empty,
        Empty,
        Empty,
        Empty>
    (Empty, navigator, runtime) {


    suspend fun signUpInfo(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, WelcomeToSignUpInfo)

}