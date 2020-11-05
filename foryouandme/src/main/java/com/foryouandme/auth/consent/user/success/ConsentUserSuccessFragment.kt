package com.foryouandme.auth.consent.user.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.auth.consent.user.ConsentUserSectionFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.consent.user.ConsentUser
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_page.*

class ConsentUserSuccessFragment : ConsentUserSectionFragment(R.layout.consent_user_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentUserAndConfiguration { config, state ->

            setupView()
            applyData(config, state.consent)

        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            consentUserFragment().toolbar.removeBackButton()

        }

    private suspend fun applyData(configuration: Configuration, consentUser: ConsentUser): Unit =
        evalOnMain {

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = consentUser.successPage,
                pageType = EPageType.SUCCESS,
                action1 = {
                    startCoroutineAsync { viewModel.integration(authNavController()) }
                },
                extraStringAction = {
                    startCoroutineAsync { viewModel.web(rootNavController(), it) }
                }
            )

        }
}