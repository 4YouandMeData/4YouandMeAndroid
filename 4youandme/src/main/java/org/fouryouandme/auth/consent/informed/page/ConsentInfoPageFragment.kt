package org.fouryouandme.auth.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import arrow.core.extensions.list.foldable.firstOrNone
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

class ConsentInfoPageFragment : ConsentInfoSectionFragment(R.layout.consent_info_page) {

    private val args: ConsentInfoPageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentInfoAndConfiguration { config, state ->

            setupView()
            applyData(config, state.consentInfo)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            consentInfoFragment().toolbar.apply {

                show()

                showBackSecondaryButton(imageConfiguration) {
                    startCoroutineAsync {
                        viewModel.back(
                            consentInfoNavController(),
                            authNavController(),
                            rootNavController()
                        )
                    }
                }

            }

        }

    private suspend fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            consentInfoFragment()
                .showAbort(
                    configuration,
                    configuration.theme.primaryColorEnd.color()
                )

            consentInfo.pages.firstOrNone { it.id == args.id }
                .map { data ->
                    page.applyData(
                        configuration = configuration,
                        page = data,
                        pageType = EPageType.INFO,
                        action1 = { option ->
                            option.fold(
                                {
                                    startCoroutineAsync {
                                        viewModel.question(
                                            consentInfoNavController(),
                                            false
                                        )
                                    }
                                },
                                {
                                    startCoroutineAsync {
                                        viewModel.page(
                                            consentInfoNavController(),
                                            it.id,
                                            false
                                        )
                                    }
                                })
                        },
                        externalAction = {
                            startCoroutineAsync { viewModel.web(rootNavController(), it) }
                        },
                        modalAction = {
                            startCoroutineAsync {
                                viewModel.modalPage(
                                    consentInfoNavController(),
                                    it.id
                                )
                            }
                        }
                    )
                }

        }
}