package com.fouryouandme.auth.welcome

import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.navigation.Navigator

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