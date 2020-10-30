package com.fouryouandme.auth.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.fouryouandme.R
import com.fouryouandme.auth.consent.informed.ConsentInfoSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.consent.informed.ConsentInfo
import com.fouryouandme.core.ext.*
import com.fouryouandme.core.view.page.EPageType
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
                    configuration.theme.primaryColorEnd.color()
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