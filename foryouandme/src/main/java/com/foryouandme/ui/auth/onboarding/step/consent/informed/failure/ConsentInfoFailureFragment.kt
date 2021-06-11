package com.foryouandme.ui.auth.onboarding.step.consent.informed.failure

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ConsentInfoPageBinding
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateEvent
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateUpdate
import com.foryouandme.ui.web.EWebPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoFailureFragment : ConsentInfoSectionFragment(R.layout.consent_info_page) {

    private val binding: ConsentInfoPageBinding?
        get() = view?.let { ConsentInfoPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ConsentInfoStateUpdate.ConsentInfo -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val consentInfo = viewModel.state.consentInfo

        if (viewBinding != null && configuration != null && consentInfo != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration = configuration,
                page = consentInfo.failurePage,
                pageType = EPageType.FAILURE,
                action1 = {
                    if (it == null) back()
                    else {
                        if (it.id == consentInfo.welcomePage.id)
                            viewModel.execute(ConsentInfoStateEvent.RestartFromWelcome)
                        else
                            viewModel.execute(ConsentInfoStateEvent.RestartFromPage(it.id))
                    }
                },
                action2 = {
                    if (it == null) back()
                    else {
                        if (it.id == consentInfo.welcomePage.id)
                            viewModel.execute(ConsentInfoStateEvent.RestartFromWelcome)
                        else
                            viewModel.execute(ConsentInfoStateEvent.RestartFromPage(it.id))
                    }
                },
                extraStringAction = { web(it, EWebPageType.LEARN_MORE) }
            )

        }
    }

}