package com.fouryouandme.auth.consent.informed.success

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.consent.informed.ConsentInfoSectionFragment
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.consent.informed.ConsentInfo
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.setStatusBar
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.core.view.page.EPageType
import kotlinx.android.synthetic.main.consent_info_page.*

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