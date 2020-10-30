package com.fouryouandme.auth.consent.informed.welcome

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.consent.informed.ConsentInfoSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.consent.informed.ConsentInfo
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.hide
import com.fouryouandme.core.ext.setStatusBar
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.core.view.page.EPageType
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

            page.applyData(
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
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )
        }
}