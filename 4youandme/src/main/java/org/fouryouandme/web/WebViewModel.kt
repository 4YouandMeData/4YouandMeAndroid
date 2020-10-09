package org.fouryouandme.web

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController

class WebViewModel(
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

    suspend fun back(rootNavController: RootNavController): Unit {
        navigator.back(rootNavController)
    }

}