package com.foryouandme.ui.auth.onboarding.step.consent.informed.welcome

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.hide
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_welcome.*

class ConsentInfoWelcomeFragment :
    ConsentInfoSectionFragment(R.layout.consent_info_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentInfoAndConfiguration { config, state ->

            setupView()
            applyData(config, state.consentInfo)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            consentInfoFragment().toolbar.hide()

        }

    private suspend fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            consentInfoFragment().hideAbort()
            consentInfoFragment()
                .consent_info_root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyDataSuspend(
                configuration = configuration,
                page = consentInfo.welcomePage,
                pageType = EPageType.INFO,
                action1 = { page ->
                    if (page == null) {
                        startCoroutineAsync {
                            viewModel.question(consentInfoNavController(), true)
                        }
                    } else {
                        startCoroutineAsync {
                            viewModel.page(
                                consentInfoNavController(),
                                page.id,
                                true
                            )
                        }
                    }
                },
                extraStringAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )
        }
}