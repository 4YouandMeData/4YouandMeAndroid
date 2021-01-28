package com.foryouandme.ui.auth.onboarding.step.screening.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.screening.Screening
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*

class ScreeningSuccessFragment : ScreeningSectionFragment(R.layout.screening_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningAndConfiguration { config, state ->

            setupView()
            applyData(config, state.screening)
        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            screeningFragment().let {
                it.toolbar.removeBackButton()
                startCoroutineAsync { it.hideAbort() }
            }

        }

    private suspend fun applyData(configuration: Configuration, screening: Screening): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = screening.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { startCoroutineAsync { screeningFragment().next() } },
                extraStringAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )

        }
}