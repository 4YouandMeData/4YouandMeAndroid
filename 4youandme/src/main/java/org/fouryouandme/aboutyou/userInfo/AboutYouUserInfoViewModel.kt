package org.fouryouandme.aboutyou.userInfo

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class AboutYouUserInfoViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<ForIO, Empty, Empty, Empty, Empty>(Empty, navigator, runtime) {

}