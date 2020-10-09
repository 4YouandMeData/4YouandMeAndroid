package org.fouryouandme.auth.optin.success

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_success.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.view.page.EPageType

class OptInSuccessFragment : OptInSectionFragment(R.layout.opt_in_success) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optInAndConfiguration { config, state ->

            setupView()
            applyData(config, state.optIns)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            optInFragment().apply {

                toolbar.removeBackButton()

            }

        }

    private suspend fun applyData(configuration: Configuration, optIns: OptIns): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = optIns.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { startCoroutineAsync { viewModel.consentUser(authNavController()) } },
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )

        }
}