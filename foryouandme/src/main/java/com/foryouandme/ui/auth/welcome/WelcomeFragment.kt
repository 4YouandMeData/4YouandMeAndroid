package com.foryouandme.ui.auth.welcome

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.ui.auth.AuthSectionFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.WelcomeBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class WelcomeFragment : AuthSectionFragment(R.layout.welcome) {

    private val viewModel: WelcomeViewModel by viewModels()

    private val binding: WelcomeBinding?
        get() = view?.let { WelcomeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is WelcomeStateUpdate.Config -> {
                        setupView()
                        applyConfiguration()
                    }
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    WelcomeLoading.Configuration ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    WelcomeError.Configuration ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(WelcomeStateEvent.GetConfiguration) }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.configuration != null) {
            setupView()
            applyConfiguration()
        } else viewModel.execute(WelcomeStateEvent.GetConfiguration)

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(WelcomeStateEvent.ScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.logo.setImageResource(imageConfiguration.logoStudy())
            viewBinding.welcomeImage.setImageResource(imageConfiguration.logoStudySecondary())
        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = viewModel.state.configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            viewBinding.start.background =
                button(configuration.theme.secondaryColor.color())
            viewBinding.start.text = configuration.text.welcome.startButton
            viewBinding.start.setTextColor(configuration.theme.primaryColorEnd.color())
            viewBinding.start.isAllCaps = false
            viewBinding.start.setOnClickListener {
                navigator.navigateTo(authNavController(), WelcomeToSignUpInfo)
            }

            viewBinding.start.alpha = 0f
            viewBinding.start.animate()
                .alpha(1f)
                .setDuration(800L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

        }
    }

}