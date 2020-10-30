package com.fouryouandme.aboutyou.integration

import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.navigation.Navigator

class AboutYouIntegrationLoginViewModel(
    navigator: Navigator,
) :
    BaseViewModel<
            Empty,
            Empty,
            Empty,
            Empty>
        (navigator, Empty)