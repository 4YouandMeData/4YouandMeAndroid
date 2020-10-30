package com.fouryouandme.auth

import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController

class AuthViewModel(navigator: Navigator) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {


    /* --- navigation --- */

    suspend fun back(
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(authNavController).not())
            navigator.back(rootNavController)
    }

}