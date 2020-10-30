package com.fouryouandme.auth.screening.failure

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.screening.ScreeningSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.screening.Screening
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.removeBackButton
import com.fouryouandme.core.ext.setStatusBar
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*

class ScreeningFailureFragment : ScreeningSectionFragment(R.layout.screening_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningAndConfiguration { config, state ->
            setupView()
            applyData(config, state.screening)
        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            screeningFragment()
                .apply {

                    toolbar.removeBackButton()
                    hideAbort()
                }

        }

    private suspend fun applyData(configuration: Configuration, screening: Screening): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = screening.failurePage,
                pageType = EPageType.FAILURE,
                action1 = {
                    startCoroutineAsync { viewModel.retryFromWelcome(screeningNavController()) }
                },
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )
        }
}