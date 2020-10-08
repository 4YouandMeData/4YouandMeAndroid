package org.fouryouandme.auth.consent.informed.success

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

class ConsentInfoSuccessFragment : ConsentInfoSectionFragment(R.layout.consent_info_page) {

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
                page = consentInfo.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { startCoroutineAsync { viewModel.consentReview(authNavController()) } },
                externalAction = { startCoroutineAsync { viewModel.web(rootNavController(), it) } }
            )

        }
}