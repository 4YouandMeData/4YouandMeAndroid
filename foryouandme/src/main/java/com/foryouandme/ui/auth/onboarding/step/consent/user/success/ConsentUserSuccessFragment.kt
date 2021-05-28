package com.foryouandme.ui.auth.onboarding.step.consent.user.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ConsentUserPageBinding
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.user.ConsentUserStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserSuccessFragment : ConsentUserSectionFragment(R.layout.consent_user_page) {

    private val binding: ConsentUserPageBinding?
        get() = view?.let { ConsentUserPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentUserStateUpdate.GetConsentUser -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setupView() {

        consentUserFragment().binding?.toolbar?.removeBackButton()

    }

    private fun applyData() {

        val configuration = configuration
        val consentUser = viewModel.state.consent
        val viewBinding = binding

        if (viewBinding != null && configuration != null && consentUser != null) {

            binding?.root?.setBackgroundColor(configuration.theme.secondaryColor.color())

            binding?.page?.applyData(
                configuration = configuration,
                page = consentUser.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { consentUserFragment().consentFragment().next() },
                extraStringAction = {
                    navigator.navigateTo(rootNavController(), AnywhereToWeb(it))
                }
            )

        }
    }

}