package org.fouryouandme.aboutyou.integration

import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouIntegrationLoginViewModel(
    navigator: Navigator,
) :
    BaseViewModel<
            Empty,
            Empty,
            Empty,
            Empty>
        (navigator, Empty)