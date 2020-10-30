package com.fouryouandme.auth.consent.user.success

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.consent.user.ConsentUserSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.consent.user.ConsentUser
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.removeBackButton
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.core.view.page.EPageType
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
                externalAction = {
                    startCoroutineAsync { viewModel.web(rootNavController(), it) }
                }
            )

        }
}