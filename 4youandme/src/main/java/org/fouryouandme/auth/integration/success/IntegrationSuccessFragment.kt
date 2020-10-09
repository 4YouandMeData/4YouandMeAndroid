package org.fouryouandme.auth.integration.success

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.IntegrationSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.*

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
                { startCoroutineAsync { viewModel.openMain(rootNavController()) } },
                { },
                { _, _ -> }
            )

        }

}