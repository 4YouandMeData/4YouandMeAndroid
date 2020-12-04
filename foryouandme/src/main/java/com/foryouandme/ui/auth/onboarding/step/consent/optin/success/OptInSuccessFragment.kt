package com.foryouandme.ui.auth.onboarding.step.consent.optin.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.optins.OptIns
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_success.*

class OptInSuccessFragment : OptInSectionFragment(R.layout.opt_in_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optInAndConfiguration { config, state ->

            setupView()
            applyData(config, state.optIns)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            optInFragment().apply {

                toolbar.removeBackButton()

            }

        }

    private suspend fun applyData(configuration: Configuration, optIns: OptIns): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = optIns.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { startCoroutineAsync { viewModel.consentUser(consentNavController()) } },
                extraStringAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )

        }
}