package org.fouryouandme.auth.welcome

import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class WelcomeFragment : BaseFragment<WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by lazy {
        viewModelFactory(this, getFactory { WelcomeViewModel(navigator, IORuntime) })
    }
}