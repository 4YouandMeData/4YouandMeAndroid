package org.fouryouandme.auth.consent.informed.success

import android.os.Bundle
import android.view.View
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.consent_info_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.view.page.EPageType

class ConsentInfoSuccessFragment : BaseFragment<ConsentInfoViewModel>(R.layout.consent_info_page) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    IORuntime
                )
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().consentInfo }
            .map { applyData(it.first, it.second) }
    }

    private fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = consentInfo.successPage,
            pageType = EPageType.SUCCESS,
            action1 = { viewModel.consentReview(rootNavController()) },
            externalAction = { viewModel.web(rootNavController(), it) }
        )

    }
}