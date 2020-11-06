package com.foryouandme.auth.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import arrow.core.extensions.list.foldable.firstOrNone
import com.foryouandme.R
import com.foryouandme.auth.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.consent.informed.ConsentInfo
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.html.setHtmlText
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_modal_page.*
import kotlinx.android.synthetic.main.consent_info_page.root

class ConsentInfoModalPageFragment : ConsentInfoSectionFragment(R.layout.consent_info_modal_page) {

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

                showCloseButton(imageConfiguration) {
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
                    args.id
                )

            consentInfo.pages.firstOrNone { it.id == args.id }
                .map { data ->
                    title.setHtmlText(data.title, true)
                    description.setHtmlText(data.body, true)
                }

        }
}