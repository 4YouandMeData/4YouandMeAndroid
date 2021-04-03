package com.foryouandme.ui.auth.onboarding.step.integration.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackSecondaryButton
import com.foryouandme.databinding.IntegrationPageBinding
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationSectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationPageFragment : IntegrationSectionFragment(R.layout.integration_page) {

    private val args: IntegrationPageFragmentArgs by navArgs()

    private val binding: IntegrationPageBinding?
        get() = view?.let { IntegrationPageBinding.bind(it) }

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

        integrationFragment()
            .binding
            ?.toolbar
            ?.showBackSecondaryButton(imageConfiguration) { back() }

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val integration = integration

        if (viewBinding != null && configuration != null && integration != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            integration.pages.firstOrNull { it.id == args.id }
                ?.let { integrationPage ->
                    viewBinding.page.applyData(
                        configuration,
                        integrationPage,
                        { nextPage(it, false) },
                        { handleSpecialLink(it) },
                        { url, nextPage -> pageToLogin(url, nextPage) }
                    )
                }

        }
    }

}