package com.foryouandme.ui.auth.onboarding.step.integration.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.web.setupWebViewWithCookies
import com.foryouandme.databinding.IntegrationLoginBinding
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationSectionFragment
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationStateEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationLoginFragment : IntegrationSectionFragment(R.layout.integration_login) {

    private val args: IntegrationLoginFragmentArgs by navArgs()

    private val binding: IntegrationLoginBinding?
        get() = view?.let { IntegrationLoginBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        applyConfiguration()
        setupWebView()

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(IntegrationStateEvent.ScreenViewed)

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    override fun onIntegrationUpdate() {
        super.onIntegrationUpdate()
        setupWebView()
    }

    private fun setupToolbar() {

        integrationFragment()
            .binding
            ?.toolbar
            ?.showCloseButton(imageConfiguration) { back() }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = configuration

        if (viewBinding != null && configuration != null) {


            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.progressBar.progressTintList =
                ColorStateList.valueOf(configuration.theme.primaryColorStart.color())

        }

    }

    private fun setupWebView() {

        val viewBinding = binding

        viewBinding?.webView
            ?.setupWebViewWithCookies(
                viewBinding.progressBar,
                args.url,
                viewModel.state.cookies,
                { handleLogin(args.nextPage) },
                { back() }
            )

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.webView?.destroy()

    }
}