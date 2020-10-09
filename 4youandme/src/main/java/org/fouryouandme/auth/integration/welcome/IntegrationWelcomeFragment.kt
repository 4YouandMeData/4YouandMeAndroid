package org.fouryouandme.auth.integration.welcome

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.integration_welcome.*
import kotlinx.android.synthetic.main.opt_in.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.IntegrationSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.ext.startCoroutineAsync

class IntegrationWelcomeFragment :
    IntegrationSectionFragment(R.layout.integration_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        integrationAndConfiguration { config, state ->

            setupView()
            applyData(config, state.integration)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            integrationFragment().toolbar.removeBackButton()
        }

    private suspend fun applyData(configuration: Configuration, integration: Integration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration,
                integration.welcomePage,
                {
                    startCoroutineAsync {
                        viewModel.nextPage(
                            integrationNavController(),
                            it,
                            true
                        )
                    }
                },
                { startCoroutineAsync { viewModel.handleSpecialLink(it) } },
                { url, nextPage ->
                    startCoroutineAsync {
                        viewModel.welcomeToLogin(
                            integrationNavController(),
                            url,
                            nextPage
                        )
                    }
                }
            )

        }
}