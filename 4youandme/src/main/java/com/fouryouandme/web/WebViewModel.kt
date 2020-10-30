package com.fouryouandme.web

import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController

class WebViewModel(
    navigator: Navigator,
) : BaseViewModel<
        Empty,
        Empty,
        Empty,
        Empty>
    (navigator, Empty) {

    /* --- navigation --- */

    suspend fun back(rootNavController: RootNavController): Unit {
        navigator.back(rootNavController)
    }

}