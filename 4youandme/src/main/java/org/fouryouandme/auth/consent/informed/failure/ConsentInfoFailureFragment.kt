package org.fouryouandme.auth.consent.informed.failure

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.consent_info_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.view.page.EPageType

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
                action1 = { option ->
                    option.fold(
                        {
                            startCoroutineAsync {
                                viewModel.back(
                                    consentInfoNavController(),
                                    authNavController(),
                                    rootNavController()
                                )
                            }
                        },
                        {
                            if (it.id == consentInfo.welcomePage.id)
                                startCoroutineAsync {
                                    viewModel.restartFromWelcome(consentInfoNavController())
                                }
                            else
                                startCoroutineAsync {
                                    viewModel.restartFromPage(consentInfoNavController(), it.id)
                                }
                        }
                    )
                },
                action2 = { option ->
                    option.fold(
                        {
                            startCoroutineAsync {
                                viewModel.back(
                                    consentInfoNavController(),
                                    authNavController(),
                                    rootNavController()
                                )
                            }
                        },
                        {
                            if (it.id == consentInfo.welcomePage.id)
                                startCoroutineAsync {
                                    viewModel.restartFromWelcome(consentInfoNavController())
                                }
                            else
                                startCoroutineAsync {
                                    viewModel.restartFromPage(consentInfoNavController(), it.id)
                                }
                        }
                    )
                },
                externalAction = {
                    startCoroutineAsync { viewModel.web(rootNavController(), it) }
                }
            )

        }
}