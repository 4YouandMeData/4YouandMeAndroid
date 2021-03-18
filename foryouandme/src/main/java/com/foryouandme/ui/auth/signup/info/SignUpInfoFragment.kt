package com.foryouandme.ui.auth.signup.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackButton
import com.foryouandme.databinding.SignUpInfoBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpInfoFragment : AuthSectionFragment(R.layout.sign_up_info) {

    private val viewModel: SignUpInfoViewModel by viewModels()

    private val binding: SignUpInfoBinding?
        get() = view?.let { SignUpInfoBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is SignUpInfoStateUpdate.Config -> {
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
                    SignUpInfoLoading.Configuration ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    SignUpInfoError.Configuration ->
                        binding?.error?.setError(it.error, null)
                        { viewModel.execute(SignUpInfoStateEvent.GetConfiguration) }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.configuration != null) {
            setupView()
            applyConfiguration()
        } else viewModel.execute(SignUpInfoStateEvent.GetConfiguration)

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(SignUpInfoStateEvent.ScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.logo.setImageResource(imageConfiguration.logo())

            viewBinding.signUp.setImageResource(imageConfiguration.nextStep())
            viewBinding.signUp.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToEnterPhone)
            }

            viewBinding.signUpText.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToEnterPhone)
            }

            viewBinding.signUpLater.setImageResource(imageConfiguration.nextStep())
            viewBinding.signUpLater.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToSignUpLater)
            }

            viewBinding.signUpLaterText.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToSignUpLater)
            }

            viewBinding.toolbar.showBackButton(imageConfiguration) { back() }

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

            viewBinding.title.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.title.setHtmlText(configuration.text.intro.title, true)

            viewBinding.description.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.description.setHtmlText(configuration.text.intro.body, true)

            viewBinding.divider.setBackgroundColor(configuration.theme.primaryColorEnd.color())

            viewBinding.signUpText.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.signUpText.text = configuration.text.intro.login

            viewBinding.signUpLaterText.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.signUpLaterText.text = configuration.text.intro.back

        }

    }

}