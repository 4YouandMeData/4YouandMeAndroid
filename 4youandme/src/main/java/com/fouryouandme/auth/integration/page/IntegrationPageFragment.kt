package com.fouryouandme.auth.integration.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.fouryouandme.R
import com.fouryouandme.auth.integration.IntegrationSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.integration.Integration
import com.fouryouandme.core.ext.*
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_page.*

class IntegrationPageFragment : IntegrationSectionFragment(R.layout.integration_page) {

    private val args: IntegrationPageFragmentArgs by navArgs()

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

            integration.pages.firstOrNull { it.id == args.id }
                ?.let { integrationPage ->
                    page.applyData(
                        configuration,
                        integrationPage,
                        {
                            startCoroutineAsync {
                                viewModel.nextPage(
                                    integrationNavController(),
                                    it,
                                    false
                                )
                            }
                        },
                        { startCoroutineAsync { viewModel.handleSpecialLink(it) } },
                        { url, nextPage ->
                            startCoroutineAsync {
                                viewModel.pageToLogin(integrationNavController(), url, nextPage)
                            }
                        }
                    )
                }

        }
}