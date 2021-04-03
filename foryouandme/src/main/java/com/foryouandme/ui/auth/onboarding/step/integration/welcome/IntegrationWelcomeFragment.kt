package com.foryouandme.ui.auth.onboarding.step.integration.welcome

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.IntegrationWelcomeBinding
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationSectionFragment
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationWelcomeToIntegrationLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationWelcomeFragment : IntegrationSectionFragment(R.layout.integration_welcome) {

    private val binding: IntegrationWelcomeBinding?
        get() = view?.let { IntegrationWelcomeBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    override fun onIntegrationUpdate() {
        super.onIntegrationUpdate()
        applyData()
    }

    private fun setupView() {

        integrationFragment().binding?.toolbar?.removeBackButton()

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val integration = integration

        if (viewBinding != null && configuration != null && integration != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration,
                integration.welcomePage,
                { nextPage(it, true) },
                { handleSpecialLink(it) },
                { url, nextPage ->
                    navigator.navigateTo(
                        integrationNavController(),
                        IntegrationWelcomeToIntegrationLogin(url, nextPage?.id)
                    )
                }
            )

        }
    }

}