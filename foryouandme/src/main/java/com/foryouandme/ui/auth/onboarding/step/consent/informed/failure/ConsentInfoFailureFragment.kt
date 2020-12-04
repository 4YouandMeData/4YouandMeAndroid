package com.foryouandme.ui.auth.onboarding.step.consent.informed.failure

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.consent_info_page.*

class ConsentInfoFailureFragment : ConsentInfoSectionFragment(R.layout.consent_info_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentInfoAndConfiguration { config, state ->

            applyData(config, state.consentInfo)

        }
    }

    private suspend fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = consentInfo.failurePage,
                pageType = EPageType.FAILURE,
                action1 = { page ->
                    if (page == null) {
                        startCoroutineAsync {
                            viewModel.back(
                                consentInfoNavController(),
                                consentNavController(),
                                onboardingStepNavController(),
                                authNavController(),
                                rootNavController()
                            )
                        }
                    } else {
                        if (page.id == consentInfo.welcomePage.id)
                            startCoroutineAsync {
                                viewModel.restartFromWelcome(consentInfoNavController())
                            }
                        else
                            startCoroutineAsync {
                                viewModel.restartFromPage(consentInfoNavController(), page.id)
                            }
                    }
                },
                action2 = { page ->
                    if (page == null) {
                        startCoroutineAsync {
                            viewModel.back(
                                consentInfoNavController(),
                                consentNavController(),
                                onboardingStepNavController(),
                                authNavController(),
                                rootNavController()
                            )
                        }
                    } else {
                        if (page.id == consentInfo.welcomePage.id)
                            startCoroutineAsync {
                                viewModel.restartFromWelcome(consentInfoNavController())
                            }
                        else
                            startCoroutineAsync {
                                viewModel.restartFromPage(consentInfoNavController(), page.id)
                            }
                    }
                },
                extraStringAction = {
                    startCoroutineAsync { viewModel.web(rootNavController(), it) }
                }
            )

        }
}