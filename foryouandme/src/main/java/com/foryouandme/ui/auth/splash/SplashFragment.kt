package com.foryouandme.ui.auth.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.databinding.SplashBinding
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SplashFragment : AuthSectionFragment(R.layout.splash) {

    private val viewModel: SplashViewModel by viewModels()

    private val binding: SplashBinding?
        get() = view?.let { SplashBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    SplashLoading.Auth ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    SplashToOnboarding,
                    SplashToWelcome ->
                        navigator.navigateTo(authNavController(), it)
                    else ->
                        navigator.navigateTo(rootNavController(), it)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.execute(SplashStateEvent.Auth)

    }

}