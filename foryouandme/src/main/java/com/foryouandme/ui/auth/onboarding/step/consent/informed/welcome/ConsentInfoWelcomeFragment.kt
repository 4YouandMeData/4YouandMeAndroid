package com.foryouandme.ui.auth.onboarding.step.consent.informed.welcome

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.hide
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ConsentInfoWelcomeBinding
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateUpdate
import com.foryouandme.ui.web.EWebPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoWelcomeFragment :
    ConsentInfoSectionFragment(R.layout.consent_info_welcome) {

    private val binding: ConsentInfoWelcomeBinding?
        get() = view?.let { ConsentInfoWelcomeBinding.bind(it) }

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

        setUpView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setUpView() {

        consentInfoFragment().binding?.toolbar?.hide()

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val consentInfo = viewModel.state.consentInfo

        if (viewBinding != null && configuration != null && consentInfo != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            consentInfoFragment().hideAbort()
            consentInfoFragment()
                .binding
                ?.consentInfoRoot
                ?.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration = configuration,
                page = consentInfo.welcomePage,
                pageType = EPageType.INFO,
                action1 = { page ->
                    if (page == null) question(true)
                    else page(page.id, true)
                },
                extraStringAction = { web(it, EWebPageType.LEARN_MORE) }
            )
        }
    }

}