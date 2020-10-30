package com.fouryouandme.auth.integration.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.fouryouandme.R
import com.fouryouandme.auth.integration.IntegrationSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.*
import com.fouryouandme.core.ext.web.setupWebViewWithCookies
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_login.*

class IntegrationLoginFragment : IntegrationSectionFragment(R.layout.integration_login) {

    private val args: IntegrationLoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        integrationAndConfiguration { config, state ->

            setupToolbar()
            applyConfiguration(config)
            setupWebView(state.cookies)

        }

    }

    private suspend fun setupToolbar(): Unit =
        evalOnMain {

            integrationFragment()
                .toolbar
                .showCloseButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            integrationNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }

        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            progress_bar.progressTintList =
                ColorStateList.valueOf(configuration.theme.primaryColorStart.color())

        }

    private suspend fun setupWebView(cookies: Map<String, String>): Unit =
        evalOnMain {

            web_view.setupWebViewWithCookies(
                progress_bar,
                args.url,
                cookies,
                {
                    startCoroutineAsync {
                        viewModel.handleLogin(integrationNavController(), args.nextPage)
                    }
                },
                {
                    startCoroutineAsync {
                        viewModel.back(
                            integrationNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }
            )

        }

    override fun onDestroyView() {
        super.onDestroyView()

        web_view.destroy()

    }
}