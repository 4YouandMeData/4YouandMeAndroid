package com.foryouandme.ui.auth.onboarding.step.integration.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.integration.Integration
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_page.*

class IntegrationSuccessFragment : IntegrationSectionFragment(R.layout.integration_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        integrationAndConfiguration { config, state ->

            setupView()
            applyData(config, state.integration)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            integrationFragment().toolbar.showBackSecondaryButton(imageConfiguration)
            {
                startCoroutineAsync {
                    viewModel.back(
                        integrationNavController(),
                        onboardingStepNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

        }

    private suspend fun applyData(configuration: Configuration, integration: Integration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration,
                integration.successPage,
                { startCoroutineAsync { integrationFragment().next() } },
                { },
                { _, _ -> }
            )

        }

}