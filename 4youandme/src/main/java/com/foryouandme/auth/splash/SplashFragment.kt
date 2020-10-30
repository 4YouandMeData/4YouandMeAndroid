package com.foryouandme.auth.splash

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.splash.*

class SplashFragment : AuthSectionFragment<SplashViewModel>(R.layout.splash) {

    override val viewModel: SplashViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { SplashViewModel(navigator, injector.authModule()) }
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