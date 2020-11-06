package com.foryouandme.auth.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.auth.consent.informed.ConsentInfoAbort
import com.foryouandme.auth.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.consent.informed.ConsentInfo
import com.foryouandme.core.ext.*
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_page.*

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
                    configuration.theme.primaryColorEnd.color(),
                    ConsentInfoAbort.FromPage(args.id)
                )

            consentInfo.pages.firstOrNull { it.id == args.id }
                ?.let { data ->
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
                        extraStringAction = {
                            startCoroutineAsync { viewModel.web(rootNavController(), it) }
                        },
                        extraPageAction = {
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