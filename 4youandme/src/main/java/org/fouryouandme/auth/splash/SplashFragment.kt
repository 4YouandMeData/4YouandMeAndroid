package org.fouryouandme.auth.splash

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.splash.*
import org.fouryouandme.R
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync

class SplashFragment : AuthSectionFragment<SplashViewModel>(R.layout.splash) {

    override val viewModel: SplashViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { SplashViewModel(navigator, IORuntime, injector.authModule()) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadingLiveData()
            .observeEvent(name()) { loading.setVisibility(it.active, false) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync { viewModel.auth(rootNavController(), authNavController()) }

    }
}