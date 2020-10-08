package org.fouryouandme.auth.screening.success

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.view.page.EPageType

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
                action1 = { startCoroutineAsync { viewModel.consentInfo(rootNavController()) } },
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )

        }
}