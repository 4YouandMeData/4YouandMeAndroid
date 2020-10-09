package org.fouryouandme.auth.screening.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

class ScreeningPageFragment : ScreeningSectionFragment(R.layout.screening_page) {

    private val args: ScreeningPageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningAndConfiguration { config, state ->
            setupView()
            applyData(config, state.screening)
        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            screeningFragment().toolbar.showBackSecondaryButton(imageConfiguration)
            {
                startCoroutineAsync {
                    viewModel.back(
                        screeningNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

        }


    private suspend fun applyData(configuration: Configuration, screening: Screening): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            screeningFragment()
                .showAbort(configuration, configuration.theme.primaryColorEnd.color())

            screening.pages.firstOrNull { it.id == args.id }
                ?.let { data ->
                    page.applyData(
                        configuration,
                        data,
                        EPageType.INFO,
                        { option ->
                            option.fold(
                                {
                                    startCoroutineAsync {
                                        viewModel.questions(
                                            screeningNavController(),
                                            false
                                        )
                                    }
                                },
                                {
                                    startCoroutineAsync {
                                        viewModel.page(
                                            screeningNavController(),
                                            it.id,
                                            false
                                        )
                                    }
                                })
                        },
                        {},
                        { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
                    )
                }

        }
}