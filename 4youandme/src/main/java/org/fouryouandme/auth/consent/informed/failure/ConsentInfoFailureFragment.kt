package org.fouryouandme.auth.consent.informed.failure

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
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

class ConsentInfoFailureFragment : BaseFragment<ConsentInfoViewModel>(R.layout.consent_info_page) {

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
            page = consentInfo.failurePage,
            pageType = EPageType.FAILURE,
            action1 = { option ->
                option.fold(
                    { viewModel.back(findNavController()) },
                    {
                        if (it.id == consentInfo.welcomePage.id)
                            viewModel.restartFromWelcome(findNavController())
                        else
                            viewModel.restartFromPage(findNavController(), it.id)
                    }
                )
            },
            action2 = { option ->
                option.fold(
                    { viewModel.back(findNavController()) },
                    {
                        if (it.id == consentInfo.welcomePage.id)
                            viewModel.restartFromWelcome(findNavController())
                        else
                            viewModel.restartFromPage(findNavController(), it.id)
                    }
                )
            },
            externalAction = { viewModel.web(rootNavController(), it) }
        )

    }
}