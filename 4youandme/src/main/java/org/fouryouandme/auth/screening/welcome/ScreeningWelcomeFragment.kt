package org.fouryouandme.auth.screening.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

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
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } })
        }
}