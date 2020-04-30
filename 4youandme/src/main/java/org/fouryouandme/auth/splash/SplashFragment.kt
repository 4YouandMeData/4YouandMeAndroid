package org.fouryouandme.auth.splash

import android.os.Bundle
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class SplashFragment : BaseFragment<SplashViewModel>() {

    override val viewModel: SplashViewModel by lazy {
        viewModelFactory(this, getFactory { SplashViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }
}