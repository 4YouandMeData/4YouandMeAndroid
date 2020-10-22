package org.fouryouandme.auth.welcome

import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.navigation.Navigator

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