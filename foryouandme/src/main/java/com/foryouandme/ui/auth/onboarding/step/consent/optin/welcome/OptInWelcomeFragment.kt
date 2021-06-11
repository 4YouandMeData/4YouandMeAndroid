package com.foryouandme.ui.auth.onboarding.step.consent.optin.welcome

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.hide
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.OptInWelcomeBinding
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInStateUpdate
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInWelcomeToOptInPermission
import com.foryouandme.ui.web.EWebPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OptInWelcomeFragment : OptInSectionFragment(R.layout.opt_in_welcome) {

    private val binding: OptInWelcomeBinding?
        get() = view?.let { OptInWelcomeBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    OptInStateUpdate.OptIn -> applyData()
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

        optInFragment().binding?.toolbar?.hide()

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val optIns = viewModel.state.optIns

        if (viewBinding != null && configuration != null && optIns != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration = configuration,
                page = optIns.welcomePage,
                pageType = EPageType.SUCCESS,
                action1 = {
                    if (viewModel.state.optIns?.permissions?.firstOrNull() != null)
                        navigator.navigateTo(
                            optInNavController(),
                            OptInWelcomeToOptInPermission(0)
                        )
                },
                extraStringAction = {
                    navigator.navigateTo(
                        rootNavController(), AnywhereToWeb(
                            it,
                            EWebPageType.LEARN_MORE
                        )
                    )
                }
            )

        }

    }

}