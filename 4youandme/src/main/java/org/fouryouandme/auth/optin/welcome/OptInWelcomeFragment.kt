package org.fouryouandme.auth.optin.welcome

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInSectionFragment
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.hide
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.view.page.EPageType

class OptInWelcomeFragment : OptInSectionFragment(R.layout.opt_in_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optInAndConfiguration { config, state ->

            setupView()
            applyConfiguration(config, state.optIns)

        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            optInFragment().toolbar.hide()

        }

    private suspend fun applyConfiguration(configuration: Configuration, optIns: OptIns): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            page.applyData(
                configuration = configuration,
                page = optIns.welcomePage,
                pageType = EPageType.SUCCESS,
                action1 = { startCoroutineAsync { viewModel.permission(optInNavController()) } },
                externalAction = {}
            )

        }

}