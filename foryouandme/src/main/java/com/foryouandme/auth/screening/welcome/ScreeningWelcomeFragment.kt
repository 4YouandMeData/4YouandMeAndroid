package com.foryouandme.auth.screening.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.auth.screening.ScreeningSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.screening.Screening
import com.foryouandme.core.ext.*
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_welcome.*

class ScreeningWelcomeFragment : ScreeningSectionFragment(R.layout.screening_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningAndConfiguration { config, state ->

            setupView()
            applyData(config, state.screening)

        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            screeningFragment().toolbar.hide()

        }

    private suspend fun applyData(configuration: Configuration, screening: Screening): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            screeningFragment().let { startCoroutineAsync { it.hideAbort() } }

            page.isVisible = true
            page.applyData(
                configuration = configuration,
                page = screening.welcomePage,
                pageType = EPageType.INFO,
                action1 = { option ->
                    option.fold(
                        {
                            startCoroutineAsync {
                                viewModel.questions(screeningNavController(), true)
                            }
                        },
                        {
                            startCoroutineAsync {
                                viewModel.page(screeningNavController(), it.id, true)
                            }
                        })
                },
                extraStringAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } })
        }
}