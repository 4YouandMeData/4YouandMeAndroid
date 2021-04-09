package com.foryouandme.ui.aboutyou.integration

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.ui.aboutyou.AboutYouSectionFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.web.getIntegrationCookies
import com.foryouandme.core.ext.web.setupWebViewWithCookies
import kotlinx.android.synthetic.main.about_you_integration_login.*

class AboutYouIntegrationLoginFragment :
    AboutYouSectionFragmentOld<AboutYouIntegrationLoginViewModel>(
        R.layout.about_you_integration_login
    ) {

    private val args: AboutYouIntegrationLoginFragmentArgs by navArgs()

    override val viewModel: AboutYouIntegrationLoginViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { AboutYouIntegrationLoginViewModel(navigator, injector.analyticsModule()) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAndConfiguration { config, user ->

            setupToolbar()
            applyConfiguration(config)
            setupWebView(user.getIntegrationCookies())

        }

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logScreenViewed() }

    }

    private suspend fun setupToolbar(): Unit =
        evalOnMain {

            toolbar
                .showCloseButton(imageConfiguration) {
                    startCoroutineAsync {
                        aboutYouViewModel.back(
                            aboutYouNavController(),
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
                        aboutYouViewModel.back(
                            aboutYouNavController(),
                            rootNavController()
                        )
                    }
                },
                {
                    startCoroutineAsync {
                        aboutYouViewModel.back(
                            aboutYouNavController(),
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