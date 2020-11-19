package com.foryouandme.auth.video

import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.arch.navigation.Navigator

class VideoViewModel(navigator: Navigator) :
    BaseViewModel<Empty, Empty, Empty, Empty>(navigator, Empty) {

    /* --- navigation --- */

    suspend fun screening(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, VideoToScreening)

}