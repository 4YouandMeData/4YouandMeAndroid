package org.fouryouandme.auth.consent.user.success

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.user.ConsentUser
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.view.page.EPageType

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