package org.fouryouandme.aboutyou.integration

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouIntegrationLoginViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) :
    BaseViewModel<
            ForIO,
            Empty,
            Empty,
            Empty,
            Empty>
        (Empty, navigator = navigator, runtime = runtime)