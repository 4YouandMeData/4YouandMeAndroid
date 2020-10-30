package com.fouryouandme.auth.optin.welcome

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.optin.OptInSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.optins.OptIns
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.hide
import com.fouryouandme.core.ext.setStatusBar
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_welcome.*

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